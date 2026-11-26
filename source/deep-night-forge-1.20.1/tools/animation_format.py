"""Preserve GeckoLib keyframes while translating unsupported source expressions."""
import math
import re


def normalize(value):
    if isinstance(value, str):
        value = re.sub(r"(?:q|query)\.property\('([^']+)'\)",
                       lambda m: 'query.head_pitch' if 'target_x_rotation' in m[1] else '0', value)
        return value.replace('(v.target_rotation??q.body_y_rotation)', 'q.body_y_rotation')
    if isinstance(value, list):
        return [normalize(item) for item in value]
    if isinstance(value, dict):
        if set(value) == {'vector'}:
            return normalize(value['vector'])
        # GeckoLib identifies constant keyframes by "vector". Renaming it to
        # "post" makes 4.8.4 interpret easing metadata as duplicate timestamps.
        # Preserve vector/easing objects and genuine timed Bedrock pre/post data.
        return {key: normalize(item) for key, item in value.items()}
    return value


def validate_animation_channels(document, label):
    """Reject malformed timestamp maps before GeckoLib can abort a pack reload."""
    count = 0
    for animation, clip in document.get('animations', {}).items():
        for bone, channels in clip.get('bones', {}).items():
            for channel in ('position', 'rotation', 'scale'):
                if channel not in channels:
                    continue
                count += 1
                value = channels[channel]
                if not isinstance(value, dict) or 'vector' in value:
                    continue
                seen = set()
                for key, frame in value.items():
                    location = f'{label}: {animation}/{bone}/{channel}/{key}'
                    try:
                        timestamp = float(key)
                    except ValueError:
                        raise AssertionError(f'{location}: expected timestamp or constant vector keyframe') from None
                    assert math.isfinite(timestamp) and timestamp >= 0, f'{location}: invalid timestamp'
                    assert timestamp not in seen, f'{location}: duplicate timestamp'
                    seen.add(timestamp)
                    if isinstance(frame, dict):
                        assert any(k in frame for k in ('vector', 'pre', 'post')), f'{location}: missing keyframe vector'
    return count
