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

package de.phoenix.submissionpipeline.api;

import java.util.ArrayList;
import java.util.List;

import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixText;

public class SubmissionTask {

    private List<APIText> classesToCompile;
    private List<APIBinary> libraries;
    private List<APIText> tests;

    public SubmissionTask() {
        this.classesToCompile = new ArrayList<APIText>();
        this.tests = new ArrayList<APIText>();
        this.libraries = new ArrayList<APIBinary>();
    }

    public void addClass(PhoenixText clazz) {
        this.classesToCompile.add(new APIText(clazz.getText(), clazz.getFullname()));
    }

    public void addTest(PhoenixText test) {
        this.tests.add(new APIText(test.getText(), test.getFullname()));
    }

    public void addLibrary(PhoenixAttachment library) {
        this.libraries.add(new APIBinary(library.getFullname(), library.getContent()));
    }

    public List<APIText> getClassesToCompile() {
        return classesToCompile;
    }

    public List<APIBinary> getLibraries() {
        return libraries;
    }

    public List<APIText> getTests() {
        return tests;
    }

}
