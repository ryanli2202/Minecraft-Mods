"""Generate verified notes for the four adjacent custom Forge projects. No other mods' text is bundled."""
import json,re
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
WORK=ROOT.parent
entries={}
def put(key,**sections):
    e=entries.setdefault(key,{'sections':{}})
    e['sections'].update({k.replace('_',' '):[v] if isinstance(v,str) else v for k,v in sections.items()})
    return e
def names(values):return ', '.join(values)
def resources(project):return WORK/project/'src/main/resources'
def habitats(res,entity):
    found=[]
    for file in (res/'data').glob('*/forge/biome_modifier/*.json'):
        j=json.loads(file.read_text(encoding='utf-8'))
        sp=j.get('spawners',[])
        if isinstance(sp,dict):sp=[sp]
        if any(s.get('type')==entity for s in sp):
            b=j.get('biomes',[])
            found.extend(b if isinstance(b,list) else [b])
    def expand(v,seen):
        if isinstance(v,dict):v=v.get('id','')
        if not str(v).startswith('#'):return [str(v)]
        if v in seen:return []
        ns,name=v[1:].split(':',1)
        f=res/'data'/ns/'tags/worldgen/biome'/f'{name}.json'
        if not f.exists():return [v]
        result=[]
        for b in json.loads(f.read_text())['values']:result.extend(expand(b,seen|{v}))
        return result
    return sorted(set(b for value in found for b in expand(value,set())))
def custom_source(e,source):e['sections']['Guide source']=[source+'; shipped custom port behavior. Server settings and datapacks may change it.']

res=resources('fantasycraft-forge-1.20.1')
for s in json.loads((res/'fantasycraft_species.json').read_text()):
    key='entity:fantasycraft:'+s['id']
    e=put(key,Creature_stats=f"Health: {s['health']}. Attack: {s['damage']}. "+('Hostile.' if s['hostile'] else 'Defender.' if s['defender'] else 'Neutral / peaceful.'))
    e['tameable']=s['tameable']
    spots=habitats(res,'fantasycraft:'+s['id'])
    if spots:put(key,Where_to_find='Natural spawn biome selections: '+names(spots)+'. Spawn caps, difficulty and configuration apply. BWG selections require BWG.')
    if s['tameable']:
        put(key,Taming=f"Right-click with {names(s['foods'])}. Each feeding has a {round(s['tameChance']*100)}% chance to tame. Hearts confirm success.",Companion_care='After taming, matching food heals 6 health or speeds up baby growth. Sneak + empty-hand right-click toggles stay/follow.')
        if s['breedFoods']:put(key,Breeding='Feed two healthy, tamed adults of the same species: '+names(s['breedFoods'])+'.')
    else:put(key,Taming='This species cannot be tamed in the supplied FantasyCraft Java port.')
    if s['rideable']:put(key,Riding=('Equip a saddle first. ' if s['saddleRequired'] else 'No saddle is required. ')+"Owner: empty-hand right-click a rideable adult. WASD to steer, Space to jump/ascend, X to descend, G to land, R for special attack, Shift to dismount. Keys are rebindable.")
    custom_source(e,'FantasyCraft 1.0.1 species catalog and Java gameplay')
for item,text in {
    'fiery_blade':'Surtr can drop Fiery Blades. Use the live crafting recipe to make a Flame Sword.',
    'minotaur_axe':'Minotaurs can drop this axe. Check the possible-drop notes and conditions.',
    'flame_sword':'Sets struck targets on fire.',
    'guide_scroll':'Given on first join when enabled; also craftable. Right-click to open the FantasyCraft guide.'}.items():put('item:fantasycraft:'+item,Use=text)

res=resources('dragonfire-forge-1.20.1')
enum=(WORK/'dragonfire-forge-1.20.1/src/main/java/dev/dragonfire/DragonBreed.java').read_text()
food=json.loads((res/'data/dragonfire/tags/items/dragon_food.json').read_text())['values']
for breed,element,weapon in re.findall(r'\("([a-z_]+)", Element\.([A-Z]+), "([a-z_]+)"',enum):
    key='entity:dragonfire:'+breed
    e=put(key,Taming='Right-click a wild dragon with '+names(food)+'. Each feeding has a 1-in-3 chance to tame.',
          Companion_care='Food heals 12 health. Feeding a baby advances growth by one minute; natural growth takes 20 minutes. Sneak + empty-hand right-click toggles stay/follow.',
          Riding='Saddle an adult dragon, then empty-hand right-click to mount. WASD to move, Space to ascend/take off, X to descend, G to land, R for elemental breath, Shift to dismount. Keys are rebindable.',
          Breeding='Feed two tamed adults of the same breed golden apples. A hatchling inherits one parent\'s owner.',
          Scales='Use shears on your own adult dragon for 1-3 scales every five minutes.',
          Element='Breath element: '+element.lower()+'. Breath does not destroy terrain.')
    e['tameable']=True
    spots=habitats(res,'dragonfire:'+breed)
    if spots:put(key,Where_to_find='Natural spawn biome selections: '+names(spots)+'. Rare spawn weight 2, group size 1; spawning conditions apply.')
    custom_source(e,'Dragonfire 1.0.1 DragonEntity and GuideBookItem')
    # Resolve actual registered resource IDs rather than inventing naming conventions.
    lang=json.loads((res/'assets/dragonfire/lang/en_us.json').read_text())
    for name in lang:
        if not name.startswith('item.dragonfire.'):continue
        item=name.split('.',2)[2]
        if item.startswith(breed+'_') and 'scale' in item:put('item:dragonfire:'+item,Where_to_find='Shear your own adult '+breed+' dragon for 1-3 matching scales every five minutes. Check live recipes for scale blocks and equipment.')
        if item.startswith(breed+'_') and ('egg' in item and 'spawn' not in item):put('item:dragonfire:'+item,Use='Use on clear ground to hatch a tamed baby '+breed+' dragon. Craft with a chicken egg surrounded by eight matching scales.')

res=resources('elemental-staffs-forge-1.20.1')
weapons={'air':'air_staff','earth':'earth_glove','electricity':'electricity_staff','fire':'fire_staff','spirit':'spirit_staff','water':'water_staff'}
abilities={'air':['Air Tornado','Air Spikes','Air Blow'],'earth':['Ground Wave','Rock Punch','Rock Wall'],'electricity':['Electric Shower','Electric Beam','Electric Outburst'],'fire':['Fire Whirlpool','Fire Breath','Fire Stun'],'spirit':['Celestial Charge','Ethereal Blades','Nebula Vortex'],'water':['Tsunami Wave Wall','Whirlpool Formation','Forming Water Bubble']}
for element,weapon in weapons.items():
    key='entity:ld_eb:'+element+'_elemental'
    e=put(key,Where_to_find='Natural spawn biome selections: '+names(habitats(res,'ld_eb:'+element+'_elemental'))+'. Monster caps and difficulty apply; no natural spawns in Peaceful.',
          Drops='Defeat this elemental for 1-4 '+element+' essence; Looting may add more.',Taming='Elementals are hostile and cannot be tamed in this Java port.',Creature_stats='50 health; 7 experience points. '+('Fireproof.' if element=='fire' else 'Can breathe and move underwater.' if element=='water' else ''))
    e['tameable']=False;custom_source(e,'Elemental Staffs 1.0.1 source and gameplay guide')
    e=put('item:ld_eb:'+weapon,Use='Right-click: '+abilities[element][0]+'. Sneak + right-click: '+abilities[element][1]+'. Right-click while airborne: '+abilities[element][2]+'. Five-second cooldown shared by all weapons of this element; no essence or durability cost.',
          Progression='Defeat matching elementals for essence. Craft the unenchanted weapon and its three ability tokens; combine all four in a crafting grid to unlock this weapon. Tokens are crafting ingredients, not independently castable spells.')
    e['categories']=['WEAPONS']
    put('item:ld_eb:'+element+'_essence',Where_to_find='Defeat '+element+' elementals. Used to craft the unenchanted weapon and ability tokens.')

res=resources('villagers-plusplus-forge-1.20.1')
catalog=json.loads((res/'assets/villagersplus/catalog.json').read_text())
regular={'neutral_villager','medieval_villager','japanese_villager','modern_villager'}
for r in catalog['villagers']:
    e=put('entity:villagersplus:'+r['id'],Where_to_find='Recruit through your Village Landmark. '+('Regular villagers cost one Iron Invitation.' if r['id'] in regular else 'Specialists cost one Emerald Invitation; at most two of each specialist per village.'),
          Taming='Recruitment is this mod\'s companion ownership system. Place a Village Landmark on open solid ground, open its menu and invite this villager. Clear ground around the landmark and leave room under the population cap.',
          Companion_care='Use the villager menu to follow, stay, roam, recall and change appearance. Feed a downed villager any food when down recovery is enabled. Mark recruitment in this checklist manually.',
          Creature_stats=f"Health: {r['health']}. Attack: {r['damage']}.")
    e['tameable']=True;custom_source(e,'Villagers++ Java VillageActions and catalog')
for item,text in {'village_landmark':'Select a design, then use on open solid ground to establish your village. Its menu recruits and manages villagers.', 'iron_invitation':'Spent at a Village Landmark to recruit one regular villager.', 'emerald_invitation':'Spent at a Village Landmark to recruit one specialist; each village allows at most two of that specialist.'}.items():put('item:villagersplus:'+item,Use=text)

vanilla={'wolf':('Bones','Approach and right-click with bones until hearts appear.'),'cat':('Raw cod or raw salmon','Approach slowly while holding fish, let the cat come close, then feed it until hearts appear.'),'parrot':('Wheat, melon, pumpkin or beetroot seeds','Right-click with seeds until hearts appear. Do not feed cookies.'),'horse':('Empty hand','Repeatedly mount an adult with an empty hand until hearts appear; then equip a saddle to steer.'),'donkey':('Empty hand','Repeatedly mount an adult with an empty hand until hearts appear. A saddle enables steering; a chest adds storage.'),'mule':('Empty hand','Breed a horse and donkey to obtain a mule. Repeatedly mount an adult until tamed.'),'llama':('Empty hand','Repeatedly mount an adult until hearts appear. A chest adds storage and carpet changes decoration; a lead directs travel.'),'trader_llama':('Empty hand','After separating from its trader, repeatedly mount until tamed. A carpet decorates it; a lead directs travel.')}
for mob,(_,text) in vanilla.items():
    e=put('entity:minecraft:'+mob,Taming=text);e['tameable']=True
for mob,text in {'camel':'Camels do not require taming. Equip a saddle to ride; they are not part of the taming checklist.', 'skeleton_horse':'Skeleton trap horses can be ridden after their riders are defeated. This is not a feeding-based taming process.', 'zombie_horse':'Not naturally obtainable or tameable in ordinary Survival; commands can create a tamed one.'}.items():
    e=put('entity:minecraft:'+mob,Taming=text);e['tameable']=False
out=ROOT/'src/main/resources/assets/deepnightguide/custom_entries.json'
out.parent.mkdir(parents=True,exist_ok=True);out.write_text(json.dumps(entries,indent=2,ensure_ascii=False),encoding='utf-8')
report={'entries':len(entries),'taming_entries':sum(e.get('tameable',False) for e in entries.values()),'custom_mods':['fantasycraft','dragonfire','ld_eb','villagersplus']}
(ROOT/'verification/custom-entry-coverage.json').write_text(json.dumps(report,indent=2))
print(json.dumps(report))
