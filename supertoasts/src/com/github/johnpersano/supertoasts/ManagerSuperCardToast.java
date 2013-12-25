/**
 *  Copyright 2013 John Persano
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package com.github.johnpersano.supertoasts;

import java.util.LinkedList;

/** Manages the life of a SuperCardToast on orientation changes. */
public class ManagerSuperCardToast {

    private static final String TAG = "Manager SuperCardToast";

    private static ManagerSuperCardToast mManagerSuperCardToast;

    private LinkedList<SuperCardToast> mList;

    private ManagerSuperCardToast() {

        mList = new LinkedList<SuperCardToast>();

    }

    protected static synchronized ManagerSuperCardToast getInstance() {

        if (mManagerSuperCardToast != null) {

            return mManagerSuperCardToast;

        } else {

            mManagerSuperCardToast = new ManagerSuperCardToast();

            return mManagerSuperCardToast;

        }

    }


    protected void add(SuperCardToast superCardToast) {

        mList.add(superCardToast);

    }

    protected void remove(SuperCardToast superCardToast) {

        mList.remove(superCardToast);

    }

    protected void clear() {

        mList.clear();

    }

    protected void clearQueue() {

        if (mList != null) {

            for (SuperCardToast superCardToast : mList) {

                if (superCardToast.isShowing()) {

                    superCardToast.getViewGroup().removeView(
                            superCardToast.getView());

                    superCardToast.getViewGroup().invalidate();

                }

            }

            mList.clear();

        }

    }

    protected LinkedList<SuperCardToast> getList() {

        return mList;

    }


}
