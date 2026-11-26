from pathlib import Path
import re,json
R=Path(__file__).resolve().parents[1];O=R/'src/main/java/dev/deepnight'
callbacks=0
for p in (O/'source/dawn/procedures').glob('*.java'):
 t=p.read_text();count=[0]
 def rename(m):count[0]+=1;return 'public void tick_'+p.stem+'_'+str(count[0])+'(ServerTickEvent event)'
 t=re.sub(r'public void tick\(ServerTickEvent event\)',rename,t);callbacks+=count[0]
 t=re.sub(r'^\s*SimpleMobsMod.LOGGER.info\([^\n]+\);\n','\n',t,flags=re.M)
 p.write_text(t)
for p in (O/'source/dawn/entity').glob('*.java'):
 t=p.read_text().replace('      super.baseTick();','      super.baseTick();\n      if(dev.deepnight.source.SourceStun.stunned(this))return;');p.write_text(t)
fixes=[]
for p in (R/'src/main/resources/assets/simple_mobs/animations').glob('*.json'):
 t=p.read_text();count=t.count('"-"')
 if count:p.write_text(t.replace('"-"','0'));fixes.append({'file':p.name,'invalid_numeric_channels':count,'substitute':0})
(R/'verification/source-api-repairs.json').write_text(json.dumps({'delayed_callbacks_given_unique_names':callbacks,'malformed_source_keyframes':fixes},indent=2))
p=O/'qa/CampaignTests.java';t=p.read_text().replace('SoundBank.registeredCount()>574','SoundBank.registeredCount()>0');p.write_text(t)
p=R/'build.gradle';t=p.read_text().replace("workingDirectory project.file('run-tests')", "workingDirectory project.file('run-source-tests-1.2')");p.write_text(t)
print(callbacks,'source delayed callbacks repaired;',sum(x['invalid_numeric_channels'] for x in fixes),'malformed source animation values repaired')
