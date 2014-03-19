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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import de.phoenix.submissionpipeline.api.APIText;
import de.phoenix.submissionpipeline.api.SubmissionTask;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompiler;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompilerException;

public class CompileTask {

    private List<APIText> classes;
    private CharSequenceCompiler<Object> compiler;
    
    public CompileTask(SubmissionTask task) {
        this.classes = task.getClassesToCompile();
        this.compiler = new CharSequenceCompiler<Object>();
    }

    public CharSequenceCompiler<Object> compile() {

        Map<String, CharSequence> map = new HashMap<String, CharSequence>();
        for (APIText text : classes) {
            map.put(text.getClassName(), text.getContent());
        }
        try {
            compiler.compile(map);
        } catch (CharSequenceCompilerException e) {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = e.getDiagnostics().getDiagnostics();
            throw new UserSubmissionException(diagnostics.toString());
        }
        
        return this.compiler;
    }

}
