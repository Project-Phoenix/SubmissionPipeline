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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submissionpipeline.api.SubmissionTask;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompiler;

public class Core {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        SubmissionTask task;
        try {
            task = mapper.readValue(System.in, SubmissionTask.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            CompileTask compileTask = new CompileTask(task);
            CharSequenceCompiler<Object> compiler = compileTask.compile();

            if (task.getTests().isEmpty()) {
                writeResult(mapper, new PhoenixSubmissionResult(SubmissionStatus.COMPILED, "Everything fine!"));
                return;
            }

            JUnitTask testTask = new JUnitTask(compiler, task);
            PhoenixSubmissionResult result = testTask.run();
            writeResult(mapper, result);
        } catch (UserSubmissionException e) {
            writeResult(mapper, new PhoenixSubmissionResult(SubmissionStatus.ERROR, e.getMessage()));
        }
    }

    private static void writeResult(ObjectMapper mapper, PhoenixSubmissionResult result) {
        try {
            mapper.writeValue(System.out, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
