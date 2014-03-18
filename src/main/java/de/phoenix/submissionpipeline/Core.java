/*
 * Copyright (C) 2014 Project-Phoenix
 * 
 * This file is part of SubmissionPipeline.
 * 
 * SubmissionPipeline is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SubmissionPipeline is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SubmissionPipeline.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.submissionpipeline;

import java.security.InvalidParameterException;

public class Core {

    public static void main(String[] args) {
        System.out.println("Started!");
        ArgumentHandler aHandler = new ArgumentHandler(args);
        if (aHandler.getClassFiles().isEmpty()) {
            throw new InvalidParameterException("Classes must not be empty!");
        }
        
        
        
        System.out.println("Ended!");
    }

}
