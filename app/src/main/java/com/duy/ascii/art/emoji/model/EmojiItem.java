/*
 * Copyright (c) 2018 by Tran Le Duy
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

package com.duy.ascii.art.emoji.model;

import java.io.Serializable;

/**
 * Created by Duy on 1/11/2018.
 */

public class EmojiItem implements Serializable {
    public static final String CHARACTER = "emoji";
    public static final String DESCRIPTION = "desc";
    private String emojiChar;
    private String desc;

    public EmojiItem(String emojiChar, String desc) {

        this.emojiChar = emojiChar;
        this.desc = desc;
    }

    public String getEmojiChar() {
        return emojiChar;
    }

    public void setEmojiChar(String emojiChar) {
        this.emojiChar = emojiChar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
