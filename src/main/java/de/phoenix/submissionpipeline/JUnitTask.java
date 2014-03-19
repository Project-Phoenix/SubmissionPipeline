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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submissionpipeline.api.APIText;
import de.phoenix.submissionpipeline.api.SubmissionTask;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompiler;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompilerException;

public class JUnitTask {

    private CharSequenceCompiler<Object> compiler;
    private SubmissionTask task;

    public JUnitTask(CharSequenceCompiler<Object> compiler, SubmissionTask task) {
        this.compiler = compiler;
        this.task = task;
    }

    public PhoenixSubmissionResult run() {

        Map<String, CharSequence> map = new HashMap<String, CharSequence>();
        for (APIText text : task.getTests()) {
            map.put(text.getClassName(), text.getContent());
        }

        try {
            Map<String, Class<Object>> compiledClasses = compiler.compile(map);
            List<Class<?>> clazzes = new ArrayList<Class<?>>(compiledClasses.values());
            Class<?>[] t = (Class<?>[]) clazzes.toArray(new Class<?>[clazzes.size()]);
            Result result = JUnitCore.runClasses(t);
            if (result.getFailureCount() == 0) {
                return new PhoenixSubmissionResult(SubmissionStatus.OK, "Tests passed!");
            } else {
                return new PhoenixSubmissionResult(SubmissionStatus.TEST_FAILED, result.getFailures().toString());
            }

        } catch (CharSequenceCompilerException e) {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = e.getDiagnostics().getDiagnostics();
            throw new UserSubmissionException(diagnostics.toString());
        }
    }

}
