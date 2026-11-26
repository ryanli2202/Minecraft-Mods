from pathlib import Path
import re,json,shutil
r=Path(__file__).resolve().parents[1]
j=r/'src/main/java/dev/deepnight'
a=r/'src/main/resources/assets'
species=(j/'Species.java').read_text()
bosses=re.findall(r'\s+(\w+)\("([^"]+)", Kind.BOSS,([^\n]+)',species)
mapping={b[0][5:].lower():b[0].lower() for b in bosses if b[0].startswith('DAWN_')}
changes=[]
for p in (j/'source/dawn').rglob('*.java'):
    t=p.read_text();old=t
    for source,target in mapping.items():
        t,n=re.subn(r'(?<=[= ])simple_mobs:'+source+r'(?=[,\]\s])','deepnight:'+target,t)
        if n:changes.append([p.name,source,target,n])
    if old!=t:p.write_text(t)
(r/'verification/source-command-remaps.json').write_text(json.dumps(changes,indent=2))
# The source pack's secondary flame controller references an absent reset clip.
# Stop that controller so the original looping base clip continues unmodified.
p=j/'source/dawn/entity/FlameBallEntity.java';t=p.read_text();t=re.sub(r'animationEvent.getController\(\).setAnimation\(RawAnimation.begin\(\).then\("animation.lava_ball3"[^\n]+','return PlayState.STOP;',t);p.write_text(t)
for name,old,new in [('SentinelKnight','animation.sentinelknight.unblock','animation.sentinelknight.shieldoff'),('CaveDweller','animation.cavedweller.dodge','animation.cavedweller.walk')]:
    p=j/f'source/dawn/entity/{name}Entity.java';p.write_text(p.read_text().replace(old,new))
# Recover the missing shared dragon attack from the source's original dragon rig.
p=a/'simple_mobs/animations/lightning_dragon.animation.json';data=json.loads(p.read_text());donor=json.loads((a/'simple_mobs/animations/dragon.animation.json').read_text());data['animations']['animation.dragon_g.attack5']=donor['animations']['animation.dragon_g.attack5'];p.write_text(json.dumps(data,separators=(',',':')))
# 1.20's default atlas only includes singular item/block directories.
p=a/'minecraft/atlases/blocks.json';p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps({'sources':[{'type':'directory','source':'items','prefix':'items/'},{'type':'directory','source':'blocks','prefix':'blocks/'}]},indent=2))
p=r/'src/main/resources/import-provenance/aod';assert p.resolve().is_relative_to(r.resolve());shutil.rmtree(p)
p=j/'GuideItem.java';t=p.read_text().replace('The original 41 battles are followed by Age of Dragons, DawnCraft and Mo\'Bosses encounters.','The campaign contains 16 DawnCraft bosses followed by 7 Mo\'Bosses encounters.').replace('Blue fire marks danger before a special attack. Move out of cones and circles. Jump the Colossus stomp. Escape the Ancient\'s cross.\\n\\nThe Deep Night alternates a close blast and an outer ring. Watch its marks.','Each boss has its own attack timing, projectiles and hitboxes. Watch its windups, movement and effects.\\n\\nLarge bosses can strike well beyond their main body.').replace('At 65% and 30% health bosses gain damage, faster special attacks, and reinforcements. A single hit is limited before armour.','Phases and special moves follow each boss\'s own rules. Some attacks can briefly stun or freeze you.');p.write_text(t)
p=j/'SummonTool.java';p.write_text(p.read_text().replace('at least 8 blocks','at least 20 blocks'))
p=r/'build.gradle';t=p.read_text();t=re.sub(r"def gecko = .*", "def gecko = 'local.fantasyraces:geckolib-forge-1.20.1-4.8.4:1.0'",t);t=t.replace("project.hasProperty('smokeTest') || project.hasProperty('drakeSizeTest')","project.hasProperty('smokeTest')");t=t.replace("                if (project.hasProperty('drakeSizeTest')) property 'deepnight.drakeSizeTest', 'true'\n",'');p.write_text(t)
p=a/'deepnight/lang/en_us.json';data=json.loads(p.read_text());data.update({'effect.deepnight.source_stun':'Stunned','effect.deepnight.source_frozen':'Frozen','effect.deepnight.source_tremor':'Tremor'});p.write_text(json.dumps(data,indent=2,ensure_ascii=False))
(r/'README.txt').write_text('''THE DEEP NIGHT 1.2.0 - FORGE 1.20.1

Install on the client and server with Forge 47.4.x, Java 17 and GeckoLib
4.8.4 for Forge 1.20.1. The install ZIP includes both required mod jars.
Replace the old Deep Night jar; keep only one version of each mod installed.
Back up existing worlds before this roster-changing upgrade. Removed entities
and items cannot be restored by the new build. Completed retained bosses are
migrated by ID; removed active encounters no longer block the campaign.

The campaign has exactly 23 bosses: 16 DawnCraft and 7 Mo'Bosses.
All other campaign bosses and all Age of Dragons content are removed.
DawnCraft entities driven by Epic Fight animation patches, plus Gael,
Morgott and the twin summoner, are excluded. Existing unrelated non-boss
Deep Night creatures, pets, weapons and enchantments remain.

Bosses instantiate the ported source entity classes, using their own combat
procedures/AI, contact timing, hitboxes, multipart helpers, movement, phases,
projectiles, particles, sounds, models, animations and renderers. The shared
Deep Night boss attack profiles, health phases and per-hit cap are removed.
Campaign unlocking, summoning, rewards and defeat credit remain Deep Night's.

The original sources target different Minecraft/loader/GeckoLib versions.
Compatibility changes are described in ASSET-NOTES.txt. Epic Fight is not
required: a local effect system supplies stun, freeze, tremor and source
impact/partial armor-negation handling. Do not install the original DawnCraft
Mobs or Mo'Bosses jars alongside this embedded port (they share asset IDs).

Defeat the Ender Dragon to obtain the Boss Summon Tool. Use it in a broad,
flat, open arena; allow at least 20 blocks overhead for the tallest bosses.
Sneak + use shows progress and the active encounter. /deepnight reclaim
restores the tool; /deepnight abandon ends a stuck fight without credit.

Build: .\\gradlew.bat build
Server verification: .\\gradlew.bat runGameTestServer
Client verification: .\\gradlew.bat runClient -PsmokeTest
Tests and the automatic client gallery are excluded from the release jar.
The source archive contains the final port; historical import generators
are not part of the supported build process.
''')
(r/'ASSET-NOTES.txt').write_text('''THE DEEP NIGHT 1.2.0 - SOURCE AND ASSET NOTES

DawnCraft boss code/assets: supplied dawncraft_mobs-1.18-1.3.9a, originally
Forge 1.18.2, net.mcreator.simplemobs. Retained source is relocated under
dev.deepnight.source.dawn. Mo'Bosses code/assets: supplied mobosses-1.0.7,
originally NeoForge 1.21.1, net.hell.mobosses, relocated under
dev.deepnight.source.mobosses. Metadata is retained in import-provenance.

Boss combat is ported source code, not the previous common attack generator.
Minecraft/Forge APIs, GeckoLib 3/4 APIs, render signatures, source entity IDs,
damage types and tick callbacks have been adapted for Forge 1.20.1.
The source attribute values and entity dimensions are retained; Minecraft's
normal attribute limits still apply (including the 1024 maximum-health cap).

Epic Fight animation-driven mobs are omitted. The retained bosses use their
own GeckoLib clips. Epic Fight hit metadata is implemented by local stun,
freeze, tremor, impact and armor-negation handlers. Local stun durations are
8/24/20/40/60 ticks by category plus a bounded impact contribution; these
are a substitute, not an emulation of Epic Fight's complete combat system.
Epic Fight's two ground-break helper visuals emit vanilla block debris;
their original source helper placement and lifetime remain. Source custom
particle providers, beam/glow renderers and projectile models are retained.

Five malformed '-' animation coordinates in Fire Giant/Herobrine were
repaired to zero. The absent Lightning Dragon attack5 clip is recovered
from the supplied shared dragon rig. Missing Sentinel unblock uses the
source shieldoff clip; Cave Dweller's missing dodge uses its source walk
clip. Flame Ball's absent secondary reset clip stops that controller while
its source base animation continues. No replacement boss attack is invented.
Source boss drop identifiers remain; seven DawnCraft drop items are plain
items, without the original mod's separate player weapon/armor systems.

Non-boss Deep Night artwork/audio remains from the user's supplied world,
souls, aerlunerpg, bosses+ and boxxexpansion packs. Existing Ember/Tide Drake
resources originate in the supplied Dragonfire assets. Existing weapon
artwork is from the supplied weapon packs. These are not dependencies.

Original code/artwork/audio authors retain their rights. No third-party
ownership or redistribution license is created by this local adaptation.
''')
lines=['THE DEEP NIGHT 1.2.0 - FINAL BOSS ROSTER','', 'Order | Name | Registry ID | Source health | Main collision width x height']
for i,(key,title,rest) in enumerate(bosses,1):
    fields=[x.strip() for x in rest.rstrip('),').split(',')]
    lines.append(f'{i:2} | {title} | deepnight:{key.lower()} | {fields[0]} | {fields[4]} x {fields[5]}')
lines+=['','16 DawnCraft bosses; 7 Mo\'Bosses. Source multipart hitboxes and attack','volumes are additional to the main collision dimensions above.']
(r/'IMPORTED-CREATURES.txt').write_text('\n'.join(lines)+'\n')
print(f'Remapped {sum(x[3] for x in changes)} source command entity selectors; updated release documentation.')
