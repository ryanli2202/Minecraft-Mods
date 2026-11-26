"""Build explicit boss attack clips and the shared server/client impact timeline.

Contact times below are taken from the supplied strike keyframes (or the source
Bedrock delayed_attack hit delay where provided). Source clips stay untouched.
Pose-only and idle-only models get authored anticipation/contact/recovery keys.
"""
from pathlib import Path
import copy, json, math

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT/'src/main/resources/assets/deepnight'
# species: (source suffix, contact seconds, melee reach, contact evidence)
CONTACTS = {
    'thorn_tyrant': ('attack', [.5], 3.5, 'body/head forward strike at 0.5'),
    'hollow_colossus': ('attack', [1.75], 5.5, 'left arm and torso swing at 1.75'),
    'rootbound_ancient': ('attack', [.5833], 4.5, 'right arm downswing at 0.5833'),
    'abyssal_demon': ('attack', [.84], 5, 'upper torso hammer sweep at 0.84'),
    'storm_herald': ('attack', [.5833], 4, 'spinning arms cross their strike pose at 0.5833'),
    'blood_sovereign': ('attack', [.25], 3.5, 'head and both arms thrust at 0.25'),
    'eclipse_wyrm': ('animation.endprowler.attack', [.5833], 5, 'jaw closes at 0.5833'),
    'stone_cyclops': ('attack', [.875, 1.875, 3.125], 5, 'initial weapon strike followed by two body/arm strikes'),
    'elder_cyclops': ('attack', [.875], 5, 'weapon strike at 0.875'),
    'elder_nosferatu': ('attack', [.25], 3.5, 'head and arm thrust at 0.25'),
    'crimson_abomination': ('attack1', [.4167], 5, 'arm and jaw strike at 0.4167'),
    'elder_forest_ent': ('attack', [.7083], 4.5, 'right arm downswing at 0.7083'),
    'mithril_golem': ('attack', [.625], 4.5, 'body and right arm strike at 0.625'),
    'walking_house': ('attack', [.4583], 5, 'body/roof slam at 0.4583'),
    'inferno_dragon': ('scratch_attack', [.64], 5, 'claw sweep at 0.64'),
    'mountain_kong': ('attack_1', [.64], 5, 'arm/body strike at 0.64'),
    'iron_overlord': ('attack_1', [2.12], 5, 'source delayed_attack: 4.92 * 0.4308943089430894'),
    'sandstorm_lizard': ('attack1', [.96], 5, 'right arm thrust at 0.96'),
    'arcane_mecha': ('attack_1', [.91], 5, 'source delayed_attack: 1.7917 * 0.5078975275'),
    'leviathan': ('attack_underwater', [.48], 5, 'head/jaw bite at 0.48'),
    'thunder_lord': ('attack', [.56], 4.5, 'root/chest strike at 0.56'),
    'frost_king': ('attack', [.64], 5, 'arm/body strike at 0.64'),
    'goblin_warlord': ('attack1', [.42], 4, 'body/waist strike at 0.42'),
    'goblin_ravager': ('attack1', [.42], 4, 'body/waist strike at 0.42'),
    'ancient_endershulk': ('animation.shapescape_mb.endershulk.knockback', [.75], 4, 'shell knockback pulse'),
    'ancient_frog_boss': ('animation.frog_boss.tongue_lash_full', [.9167], 5, 'tongue reaches full extension at 0.9167'),
    'ancient_golem_boss': ('animation.shapescape_mb.golem_boss.attack_punch', [.9583, 2.5, 4.25], 5, 'three downward punch contacts in main bone position'),
    'ancient_mammoth_boss': ('animation.shapescape_mb.mammoth_boss.attack_right', [.375], 5, 'head/tusk sweep midpoint'),
    'ancient_phantom_boss': ('animation.shapescape_mb.phantom_boss.melee_attack', [.6667], 5, 'jaw closes at 0.6667'),
    'ancient_piglin_boss': ('animation.shapescape_mb.piglin_boss.sword_attack', [1.125], 5, 'right arm reaches sword contact at 1.125'),
    'ancient_sculkpion_boss': ('animation.shapescape_mb.sculkpion_boss.attack_left', [.5], 5, 'left claw closes at 0.5'),
    'ancient_yeti_boss': ('animation.shapescape_mb.yeti_boss.melee_attack', [1.5], 5, 'body/arms strike at 1.5'),
}
POSES = {'ashen_knight': ('rightArm', 'body', 3.3), 'cinder_gargoyle': ('rightArm', 'body', 4.5)}
AUTHORED = {
    'abyssal_kraken': ('bone2', ['tentacle', 'tentacle7', 'tentacle8', 'tentacle4'], 5.5),
    'three_headed_hydra': ('bone21', ['head', 'head2', 'head3'], 5),
    'starborn_tyrant': ('bone3', ['big_paw_right', 'big_paw_left'], 5),
    'foreign_matriarch': ('bone3', ['paw', 'paw11'], 4.5),
    'alien_centipede': ('bone2', ['head'], 4.5),
    'giftbound_evergreen': ('body', ['right_arm', 'left_arm'], 5),
    'the_deep_night': ('body', ['rightarm', 'leftarm'], 5.5),
}

def first(value, default):
    if isinstance(value, dict):
        for key in ('vector', 'post', 'pre'):
            if key in value: return first(value[key], default)
        return first(next(iter(value.values()), default), default)
    if isinstance(value, (int, float)): return [value]*3
    if isinstance(value, list): return [v if isinstance(v, (int, float)) else default[i] for i,v in enumerate(value)]
    return default[:]

def pose(clip):
    return {bone: {channel: first(value, [1,1,1] if channel == 'scale' else [0,0,0])
                   for channel,value in channels.items() if channel in ('rotation','position','scale')}
            for bone,channels in clip.get('bones', {}).items()}

def curve(base, windup, contact, end=1.4):
    return {'0.0': base, '0.5': windup, '0.8': contact, '0.95': contact, str(end): base}

def authored(name, clips, geometry):
    idle = pose(clips['idle']); bones = copy.deepcopy(idle)
    names = {b['name'] for b in geometry['minecraft:geometry'][0]['bones']}
    if name in POSES:
        arm,body,reach=POSES[name]; target=pose(clips['source.attack'])
        for bone,channels in target.items():
            for channel,end in channels.items():
                base=idle.get(bone,{}).get(channel,[1,1,1] if channel=='scale' else [0,0,0])
                bones.setdefault(bone,{})[channel]=curve(base,base,end)
        base=idle.get(arm,{}).get('rotation',[0,0,0]); strike=target[arm]['rotation']
        bones[arm]['rotation']=curve(base,[-100,base[1],base[2]-15],strike)
        base=idle.get(body,{}).get('rotation',[0,0,0])
        bones.setdefault(body,{})['rotation']=curve(base,[base[0],base[1]+25,base[2]],target[body]['rotation'])
        evidence='Authored anticipation and recovery around the supplied static attack pose'
    else:
        body,parts,reach=AUTHORED[name]
        for bone in [body]+parts: assert bone in names,(name,bone)
        for bone in parts:
            base=idle.get(bone,{}).get('rotation',[0,0,0])
            bones.setdefault(bone,{})['rotation']=curve(base,[base[0]-55,base[1],base[2]],[base[0]+35,base[1],base[2]])
        base=idle.get(body,{}).get('rotation',[0,0,0])
        bones.setdefault(body,{})['rotation']=curve(base,[base[0]-12,base[1],base[2]],[base[0]+18,base[1],base[2]])
        evidence='Authored windup, strike and recovery; supplied model has only an idle clip'
    return {'loop':False,'animation_length':1.4,'bones':bones},[.8],reach,evidence

def main():
    rows=json.loads((ROOT/'tools/expanded_species.json').read_text());profiles={};audit=[]
    for row in rows:
        if row['kind']!='BOSS':continue
        name=row['id'];path=ASSETS/f'animations/{name}.animation.json';doc=json.loads(path.read_text());clips=doc['animations']
        if name in CONTACTS:
            source,contacts,reach,evidence=CONTACTS[name];clip=copy.deepcopy(clips['source.'+source])
        else:
            clip,contacts,reach,evidence=authored(name,clips,json.loads((ASSETS/f'geo/{name}.geo.json').read_text()));source='authored'
        # GeckoLib 4 registers query.anim_time but does not expand Bedrock's q.* alias.
        clip=json.loads(json.dumps(clip).replace('q.anim_time','query.anim_time'))
        duration=clip['animation_length'];assert all(0<t<duration for t in contacts),(name,contacts,duration)
        model_bones={b['name'] for b in json.loads((ASSETS/f'geo/{name}.geo.json').read_text())['minecraft:geometry'][0]['bones']}
        unused=sorted(set(clip['bones'])-model_bones)
        # Shared source clips include optional saddles, extra necks and mount bones.
        clip['bones']={bone:channels for bone,channels in clip['bones'].items() if bone in model_bones}
        assert clip['bones'],name
        clip['loop']=False
        # The same markers drive server damage and client QA; no renderer callback deals damage.
        clip['timeline']={str(t): 'deepnight:impact' for t in contacts}
        clips['attack']=clip;path.write_text(json.dumps(doc,indent=2)+'\n')
        profiles[name]={'durationTicks':duration*20,'impactTicks':[t*20 for t in contacts],'reach':reach}
        audit.append({'boss':name,'source':source,'contact_seconds':contacts,'duration_seconds':duration,'evidence':evidence,'unused_optional_source_bones':unused})
    assert len(profiles)==41
    (ROOT/'src/main/resources/data/deepnight/boss_attacks.json').write_text(json.dumps(profiles,indent=2)+'\n')
    (ROOT/'verification/boss-attack-timings.json').write_text(json.dumps(audit,indent=2)+'\n')
    print(f'Built {len(profiles)} explicit boss attack timelines; {len(POSES)} pose attacks and {len(AUTHORED)} missing attacks animated.')

if __name__=='__main__':main()
