package com.github.lunatrius.msh.reference;

import com.github.lunatrius.core.util.vector.Vector3f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
    public static final String MODID = "MonsterSpawnHighlighter";
    public static final String NAME = "Monster Spawn Highlighter";
    public static final String VERSION = "${version}";
    public static final String FORGE = "${forgeversion}";
    public static final String MINECRAFT = "${mcversion}";
    public static final String PROXY_SERVER = "com.github.lunatrius.msh.proxy.ServerProxy";
    public static final String PROXY_CLIENT = "com.github.lunatrius.msh.proxy.ClientProxy";
    public static final String GUI_FACTORY = "com.github.lunatrius.msh.client.gui.GuiFactory";

    public static final Vector3f PLAYER_POSITION = new Vector3f();

    public static Logger logger = LogManager.getLogger(Reference.MODID);

    public static boolean hasSeed = false;
    public static long seed = 0;
}
