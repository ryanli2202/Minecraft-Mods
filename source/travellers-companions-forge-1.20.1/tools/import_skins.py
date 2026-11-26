"""Copy skins; expand the legacy 64x32 Varys skin using Minecraft's mirrored limb layout."""
from pathlib import Path
from PIL import Image
import shutil,json,hashlib
root=Path(__file__).resolve().parents[1]
dest=root/'src/main/resources/assets/travellerscompanions/textures/entity';dest.mkdir(parents=True,exist_ok=True)
manifest={}
for src in (root.parent/'travellerscompanions').glob('*.png'):
    image=Image.open(src).convert('RGBA')
    if image.size==(64,32):
        result=Image.new('RGBA',(64,64));result.paste(image,(0,0))
        # Mojang's legacy skin remap: six faces each for the left leg and arm.
        maps=[((4,16,8,20),(20,48)),((8,16,12,20),(24,48)),((8,20,12,32),(16,52)),((4,20,8,32),(20,52)),((0,20,4,32),(24,52)),((12,20,16,32),(28,52)),((44,16,48,20),(36,48)),((48,16,52,20),(40,48)),((48,20,52,32),(32,52)),((44,20,48,32),(36,52)),((40,20,44,32),(40,52)),((52,20,56,32),(44,52))]
        for rect,at in maps:result.paste(image.crop(rect).transpose(Image.Transpose.FLIP_LEFT_RIGHT),at)
        result.save(dest/src.name)
    else:
        assert image.size==(64,64),src
        shutil.copyfile(src,dest/src.name)
    manifest[src.name]={'original_size':image.size,'source_sha256':hashlib.sha256(src.read_bytes()).hexdigest(),'output_sha256':hashlib.sha256((dest/src.name).read_bytes()).hexdigest()}
(root/'verification/skins.json').write_text(json.dumps(manifest,indent=2))
print(f'Imported {len(manifest)} skins; expanded legacy Varys UV layout.')
