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

package com.duy.ascii.sharedcode.asciiart;

import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */
class AsciiArtContract {
    public interface View {
        void showProgress();

        void hideProgress();

        void display(ArrayList<String> list);

        void setPresenter(Presenter presenter);

        void append(String value);
    }

    public interface Presenter {
        void load(int index);

        void stop();
    }
}
