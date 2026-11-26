"""Regressions for GeckoLib 4.8.4's stricter keyframe parser."""
import copy
import unittest
from animation_format import normalize, validate_animation_channels


def document(channel):
    return {'animations': {'idle': {'bones': {'root': {'position': channel}}}}}


class AnimationFormatTests(unittest.TestCase):
    def test_constant_eased_vector_retains_discriminator_and_curve(self):
        source = {'vector': [0, -1, 0], 'easing': 'easeInOutSine', 'easingArgs': [2]}
        original = copy.deepcopy(source)
        result = normalize(document(source))
        self.assertEqual(result, document(original))
        self.assertEqual(source, original)
        self.assertEqual(validate_animation_channels(result, 'constant'), 1)

    def test_timed_gecko_and_bedrock_keyframes_preserve_timing(self):
        source = document({'0.0': {'vector': [1, 2, 3], 'easing': 'easeOutBounce'},
                           '0.5': {'pre': [3, 2, 1], 'post': [0, 1, 2], 'lerp_mode': 'catmullrom'}})
        self.assertEqual(normalize(source), source)
        validate_animation_channels(normalize(source), 'timed')

    def test_plain_vectors_and_source_expression_translation(self):
        result = normalize(document({'vector': ["query.property('target_x_rotation')", "q.property('unknown')", 0]}))
        self.assertEqual(result, document(['query.head_pitch', '0', 0]))
        validate_animation_channels(result, 'plain')

    def test_rejects_previous_importer_output(self):
        with self.assertRaisesRegex(AssertionError, 'expected timestamp'):
            validate_animation_channels(document({'post': [0, -1, 0], 'easing': 'linear'}), 'broken')

    def test_rejects_ambiguous_or_invalid_timestamps(self):
        for channel in ({'0': [0, 0, 0], '0.0': [1, 1, 1]}, {'NaN': [0, 0, 0]}, {'-1': [0, 0, 0]}):
            with self.subTest(channel=channel), self.assertRaises(AssertionError):
                validate_animation_channels(document(channel), 'broken')


if __name__ == '__main__':
    unittest.main()
