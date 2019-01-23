/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.comfoair.datatypes;

import org.openhab.binding.comfoair.handling.ComfoAirCommandType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle revolutions per minute values
 *
 * @author Grzegorz Miasko
 * @since 1.14.0
 */
public class DataTypeRPM implements ComfoAirDataType {

    private static final Logger logger = LoggerFactory.getLogger(DataTypeRPM.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public State convertToState(int[] data, ComfoAirCommandType commandType) {

        if (data == null || commandType == null) {
            logger.error("Error of \"DataTypeRPM\" class \"convertToState\" method parameter: null");
            return null;
        } else {

            int[] get_reply_data_pos = commandType.getGetReplyDataPos();

            int value = 0;
            int base = 0;

            for (int i = get_reply_data_pos.length - 1; i >= 0; i--) {

                if (get_reply_data_pos[i] < data.length) {
                    value += data[get_reply_data_pos[i]] << base;
                    base += 8;
                } else {
                    return null;
                }
            }

            return new DecimalType((int) (1875000.0 / value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] convertFromState(State value, ComfoAirCommandType commandType) {

        if (value == null || commandType == null) {
            logger.error("Error of \"DataTypeRPM\" class \"convertFromState\" method parameter: null");
            return null;
        } else {

            int[] template = commandType.getChangeDataTemplate();

            template[commandType.getChangeDataPos()] = (int) (1875000 / ((DecimalType) value).doubleValue());

            return template;
        }
    }

}
