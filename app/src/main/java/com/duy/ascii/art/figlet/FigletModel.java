/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ascii.art.figlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * FigletFont implementation. A single static method call will create the ascii
 * art in a mulitilne String. FigletFont format is specified at:
 * https://github.com/lalyos/jfiglet/blob/master/figfont.txt
 * <p>
 * <pre>
 * <code>String asciiArt = FigletFont.convertOneLine("hello");</code>
 * </pre>
 * <p>
 * Originally found at: http://www.rigaut.com/benoit/CERN/FigletJava/. Moved to
 * <a href="http://lalyos.github.io/jfiglet/">github.com</a>.
 *
 * @author Benoit Rigaut CERN July 96
 *         www.rigaut.com benoit@rigaut.com
 *         released with GPL the 13th of november 2000 (my birthday!)
 */
public class FigletModel {
    final public static int MAX_CHARS = 1024;
    public char hardblank;
    public int height = -1;
    public int heightWithoutDescenders = -1;
    public int maxLine = -1;
    public int smushMode = -1;
    public char font[][][] = null;
    public String fontName = null;

    /**
     * Creates a FigletFont as specified at: https://github.com/lalyos/jfiglet/blob/master/figfont.txt
     *
     * @param stream
     * @throws IOException
     */
    public FigletModel(InputStream stream) throws IOException {
        font = new char[MAX_CHARS][][];
        BufferedReader data = null;
        String dummyS;
        int dummyI;
        int charCode;

        String codeTag;
        try {

            data = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream), "UTF-8"));

            dummyS = data.readLine();
            StringTokenizer st = new StringTokenizer(dummyS, " ");
            String s = st.nextToken();
            hardblank = s.charAt(s.length() - 1);
            height = Integer.parseInt(st.nextToken());
            heightWithoutDescenders = Integer.parseInt(st.nextToken());
            maxLine = Integer.parseInt(st.nextToken());
            smushMode = Integer.parseInt(st.nextToken());
            dummyI = Integer.parseInt(st.nextToken());

            /*
             * try to read the font name as the first word of the first comment
             * line, but this is not standardized !
             */
            st = new StringTokenizer(data.readLine(), " ");
            if (st.hasMoreElements())
                fontName = st.nextToken();
            else
                fontName = "";

            for (int i = 0; i < dummyI - 1; i++) // skip the comments
                dummyS = data.readLine();
            charCode = 31;
            while (dummyS != null) {  // for all the characters
                //System.out.print(i+":");
                charCode++;
                for (int h = 0; h < height; h++) {
                    dummyS = data.readLine();
                    if (dummyS != null) {
                        //System.out.println(dummyS);
                        int iNormal = charCode;
                        boolean abnormal = true;
                        if (h == 0) {
                            try {
                                codeTag = dummyS.concat(" ").split(" ")[0];
                                if (codeTag.length() > 2 && "x".equals(codeTag.substring(1, 2))) {
                                    charCode = Integer.parseInt(codeTag.substring(2), 16);
                                } else {
                                    charCode = Integer.parseInt(codeTag);
                                }
                            } catch (NumberFormatException e) {
                                abnormal = false;
                            }
                            if (abnormal)
                                dummyS = data.readLine();
                            else
                                charCode = iNormal;
                        }
                        if (h == 0)
                            font[charCode] = new char[height][];
                        int t = dummyS.length() - 1 - ((h == height - 1) ? 1 : 0);
                        if (height == 1)
                            t++;
                        font[charCode][h] = new char[t];
                        for (int l = 0; l < t; l++) {
                            char a = dummyS.charAt(l);
                            font[charCode][h][l] = (a == hardblank) ? ' ' : a;
                        }
                    }
                }
            }
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    /**
     * Returns all character from this Font. Each character is defined as
     * char[][]. So the whole font is a char[][][].
     *
     * @return The representation of all characters.
     */
    public char[][][] getFont() {
        return font;
    }

    /**
     * Return a single character represented as char[][].
     *
     * @param c The numerical id of the character.
     * @return The definition of a single character.
     */
    public char[][] getChar(int c) {
        return font[c];
    }

    /**
     * Selects a single line from a character.
     *
     * @param c Character id
     * @param l Line number
     * @return The selected line from the character
     */
    public String getCharLineString(int c, int l) {
        if (font[c][l] == null)
            return null;
        else {
            return new String(font[c][l]);
        }
    }

    public String convert(String message) {
        String result = "";
        for (int l = 0; l < this.height; l++) { // for each line
            for (int c = 0; c < message.length(); c++)
                // for each char
                result += this.getCharLineString((int) message.charAt(c), l);
            result += '\n';
        }
        return result;
    }
}
