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

import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import de.phoenix.submissionpipeline.api.SubmissionTask;

public class Core {
    public static void main(String[] args) {

        System.out.println("Started");
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\Z");
        String next = scanner.next();
        scanner.close();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper = mapper.registerModule(new JodaModule());
            SubmissionTask task = mapper.readValue(next, SubmissionTask.class);
            // TODO: Handle the task
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }

}
