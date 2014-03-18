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
import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse the argument line. <br>
 * The order of the arguments is: <br>
 * 1. Class Files to compile. First argument of the list must be -c <br>
 * 2. Test Files to compile. First argument of the list must be -t <br>
 * 3. Libraries to include. First argument of the list must be -l
 * 
 */
public class ArgumentHandler {

    private List<String> libraries;
    private List<String> classFiles;
    private List<String> testFiles;

    public ArgumentHandler(String[] args) {
        if (args == null || args.length == 0) {
            throw new InvalidParameterException("Args cannot be null or empty!");
        }

        this.libraries = new ArrayList<String>();
        this.classFiles = new ArrayList<String>();
        this.testFiles = new ArrayList<String>();

        this.parse(args);
    }

    private void parse(String[] args) {
        int p = 0;
        // class files to compile
        if (args[p].equalsIgnoreCase("-c")) {
            ++p;
            while (p < args.length && !args[p].equalsIgnoreCase("-t")) {
                this.classFiles.add(args[p++]);
            }
        }

        // Test files to compile
        if (p != args.length && args[p].equalsIgnoreCase("-t")) {
            ++p;
            while (p < args.length && !args[p].equalsIgnoreCase("-l")) {
                this.testFiles.add(args[p++]);
            }
        }

        // libraries
        if (p != args.length && args[p].equalsIgnoreCase("-l")) {
            ++p;
            while (p < args.length) {
                this.libraries.add(args[p++]);
            }
        }

    }

    public List<String> getClassFiles() {
        return classFiles;
    }

    public List<String> getLibraries() {
        return libraries;
    }

    public List<String> getTestFiles() {
        return testFiles;
    }

}
