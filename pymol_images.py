from pymol.cmd import *
def get_molfile(name):
    load("data/%s_3D.mol" % name)
    show("spheres", "%s_3D" % name)
    util.cbaw()
extend("get_molfile", get_molfile)

def make_image(name):
    ray()
    png("img/%s_3D.png" % name)
extend("make_image", make_image)
