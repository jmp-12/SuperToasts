/**
 *  Copyright 2014 John Persano
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

/**
 * Manages the life of a SuperCardToast on orientation changes.
 */
class ManagerSuperCardToast {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "Manager SuperCardToast";

    private static ManagerSuperCardToast mManagerSuperCardToast;

    private final LinkedList<SuperCardToast> mList;

    private ManagerSuperCardToast() {

        mList = new LinkedList<SuperCardToast>();

    }

    /**
     * Singleton method to ensure all SuperCardToasts are passed through the same manager.
     */
    protected static synchronized ManagerSuperCardToast getInstance() {

        if (mManagerSuperCardToast != null) {

            return mManagerSuperCardToast;

        } else {

            mManagerSuperCardToast = new ManagerSuperCardToast();

            return mManagerSuperCardToast;

        }

    }

    /**
     * Add a SuperCardToast to the list.
     */
    void add(SuperCardToast superCardToast) {

        mList.add(superCardToast);

    }

    /**
     * Removes a SuperCardToast from the list.
     */
    void remove(SuperCardToast superCardToast) {

        mList.remove(superCardToast);

    }

    /**
     * Removes all SuperCardToasts and clears the list
     */
    void cancelAllSuperActivityToasts() {

        for (SuperCardToast superCardToast : mList) {

            if (superCardToast.isShowing()) {

                superCardToast.getViewGroup().removeView(
                        superCardToast.getView());

                superCardToast.getViewGroup().invalidate();

            }

        }

        mList.clear();

    }

    /**
     * Used in SuperCardToast saveState().
     */
    LinkedList<SuperCardToast> getList() {

        return mList;

    }


}
