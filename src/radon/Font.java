package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Font {

    private static String chars = "" + //
            "ABCDEFGHIJKL" + //
            "MNOPQRSTUVWXYZ0123456789" + //
            " .,!?\u203D<>:;[]'\"-+=/\\%()" + //
            "";

    public static final int[] charWidth = new int[49];

    public static final int[] collection = {Input.KEY_0, Input.KEY_1, Input.KEY_2, Input.KEY_3, Input.KEY_4, Input.KEY_5, Input.KEY_6,
            Input.KEY_7, Input.KEY_8, Input.KEY_9, Input.KEY_NUMPAD0, Input.KEY_NUMPAD1, Input.KEY_NUMPAD2, Input.KEY_NUMPAD3,
            Input.KEY_NUMPAD4, Input.KEY_NUMPAD5, Input.KEY_NUMPAD6, Input.KEY_NUMPAD7, Input.KEY_NUMPAD8, Input.KEY_NUMPAD9, Input.KEY_A,
            Input.KEY_B, Input.KEY_C, Input.KEY_D, Input.KEY_E, Input.KEY_F, Input.KEY_G, Input.KEY_H, Input.KEY_I, Input.KEY_J,
            Input.KEY_K, Input.KEY_L, Input.KEY_M, Input.KEY_N, Input.KEY_O, Input.KEY_P, Input.KEY_Q, Input.KEY_R, Input.KEY_S,
            Input.KEY_T, Input.KEY_U, Input.KEY_V, Input.KEY_W, Input.KEY_X, Input.KEY_Y, Input.KEY_Z,};

    public static void draw(String s, int x, int y, int scale, Graphics g) throws SlickException {
        s = s.toUpperCase();
        Image font = Game.scaleImage(new Image("res/images/font.png"), scale);
        int offset = 0;
        for (int i = 0; i < s.length(); i++) {
            int[] pos = new int[2];
            pos = getCharImage(s.charAt(i));
            pos[0] *= scale;
            pos[1] *= scale;
            if (pos[0] >= 0 && pos[1] >= 0) {
                g.drawImage(font, x + offset, y, x + offset + 6 * scale, y + 7 * scale, pos[0], pos[1], pos[0] + 6 * scale, pos[1] + 7
                        * scale, g.getColor());
                offset += getCharWidth(s.charAt(i), scale);
            }
        }
    }

    public static void centerText(String s, int x, int y, int scale, Graphics g) throws SlickException {
        int width = 0;
        s = s.toUpperCase();
        for (int i = 0; i < s.length(); i++) {
            width += getCharWidth(s.charAt(i), scale);
        }
        int newX = x - width / 2;
        draw(s, newX, y, scale, g);
    }

    public static int getWidth(String s, int scale) {
        int width = 0;
        s = s.toUpperCase();
        for (int i = 0; i < s.length(); i++) {
            width += getCharWidth(s.charAt(i), scale);
        }
        return width;
    }

    public static int[] getCharImage(char c) {
        int tileNumber = chars.indexOf(c);
        int[] pos = new int[2];
        pos[0] = tileNumber % 12 * 6;
        pos[1] = tileNumber / 12 * 7;
        return pos;
    }

    public static int getCharWidth(char c, int scale) {
        return getCharWidth(chars.indexOf(c), scale);
    }

    private static int getCharWidth(int tile, int scale) {
        charWidth[0] = 4;
        charWidth[1] = 4;
        charWidth[2] = 3;
        charWidth[3] = 4;
        charWidth[4] = 3;
        charWidth[5] = 3;
        charWidth[6] = 4;
        charWidth[7] = 4;
        charWidth[8] = 3;
        charWidth[9] = 4;
        charWidth[10] = 4;
        charWidth[11] = 3;

        charWidth[12] = 5;
        charWidth[13] = 4;
        charWidth[14] = 4;
        charWidth[15] = 4;
        charWidth[16] = 4;
        charWidth[17] = 4;
        charWidth[18] = 4;
        charWidth[19] = 3;
        charWidth[20] = 4;
        charWidth[21] = 4;
        charWidth[22] = 5;
        charWidth[23] = 4;

        charWidth[24] = 4;
        charWidth[25] = 3;
        charWidth[26] = 4;
        charWidth[27] = 2;
        charWidth[28] = 4;
        charWidth[29] = 4;
        charWidth[30] = 4;
        charWidth[31] = 4;
        charWidth[32] = 4;
        charWidth[33] = 4;
        charWidth[34] = 4;
        charWidth[35] = 4;

        charWidth[36] = 2;
        charWidth[37] = 1;
        charWidth[38] = 2;
        charWidth[39] = 1;
        charWidth[40] = 4;
        charWidth[41] = 4;
        charWidth[42] = 3;
        charWidth[43] = 3;
        charWidth[44] = 1;
        charWidth[45] = 1;
        charWidth[46] = 2;
        charWidth[47] = 2;

        for (int i = 0; i < charWidth.length; i++) {
            charWidth[i] *= scale;
            charWidth[i] += scale;
        }

        return charWidth[tile];
    }

    public static String editString(String s, GameContainer container) {
        Input input = container.getInput();

        for (int key : collection) {
            if (input.isKeyPressed(key)) {
                String keyString = Input.getKeyName(key);
                if (keyString.startsWith("NUMPAD")) {
                    keyString = keyString.substring(keyString.length() - 1);
                }
                return s + keyString;
            }
        }
        if (input.isKeyPressed(Input.KEY_BACK) && s.length() > 0) {
            return s.substring(0, s.length() - 1);
        }
        if (input.isKeyPressed(Input.KEY_SPACE) && s.length() > 0) {
            return s + " ";
        }
        return s;

    }

    public static void resetInput(GameContainer container) {
        Input input = container.getInput();
        for (int key : collection) {
            input.isKeyPressed(key);
        }
        input.isKeyPressed(Input.KEY_BACK);
        input.isKeyPressed(Input.KEY_SPACE);
    }

}
