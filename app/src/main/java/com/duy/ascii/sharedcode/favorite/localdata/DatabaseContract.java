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

package com.duy.ascii.sharedcode.favorite.localdata;

import java.util.ArrayList;

/**
 * Created by Duy on 09-Jul-17.
 */

public class DatabaseContract {
    public interface View {
        void show(ArrayList<TextItem> items);

        void insert(TextItem item, int pos);

        void delete(int pos);
    }

    public interface Presenter {
        void update(TextItem item);

        void delete(TextItem item);

        void insert(TextItem item);

    }
}
