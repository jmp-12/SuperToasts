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

import com.github.johnpersano.supertoasts.SuperToast;

/** Creates a reference to basic style options so that all types of SuperToasts
 *  will be themed the same way in a particular class. */
public class DefaultStyle {

    public SuperToast.Animations animations;
    public int duration;
    public int background;
    public int typefaceStyle;
    public int textColor;

}