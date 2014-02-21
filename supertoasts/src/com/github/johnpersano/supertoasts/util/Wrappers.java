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

package com.github.johnpersano.supertoasts.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to store any OnClickWrappers and OnDismissWrappers set to SuperActivityToasts/SuperCardToasts.
 * This should be sent through the onRestoreState() methods of the SuperActivityToasts/SuperCardToasts
 * classes in order for those methods to reattach any listeners.
 */
public class Wrappers {

    private List<OnClickWrapper> onClickWrapperList = new ArrayList<OnClickWrapper>();

    private List<OnDismissWrapper> onDismissWrapperList = new ArrayList<OnDismissWrapper>();

    /**
     * Adds an onclickwrapper to a list that will be reattached on orientation change.
     *
     * @param onClickWrapper {@link OnClickWrapper}
     */
    public void add(OnClickWrapper onClickWrapper){

        onClickWrapperList.add(onClickWrapper);

    }

    /**
     * Adds an ondismisswrapper to a list that will be reattached on orientation change.
     *
     * @param onDismissWrapper {@link OnDismissWrapper}
     */
    public void add(OnDismissWrapper onDismissWrapper){

        onDismissWrapperList.add(onDismissWrapper);

    }

    /**
     * Used during recreation of SuperActivityToasts/SuperCardToasts.
     */
    public List<OnClickWrapper> getOnClickWrappers() {

        return onClickWrapperList;

    }

    /**
     * Used during recreation of SuperActivityToasts/SuperCardToasts.
     */
    public List<OnDismissWrapper> getOnDismissWrappers() {

        return onDismissWrapperList;

    }

}
