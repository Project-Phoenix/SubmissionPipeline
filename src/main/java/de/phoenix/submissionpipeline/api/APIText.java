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

public class APIText {

    private String content;
    private String className;

    protected APIText() {

    }

    public APIText(String content, String className) {
        this.content = content;
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "APIText={ClassName:" + className + ";Content:" + content + "}";
    }
}
