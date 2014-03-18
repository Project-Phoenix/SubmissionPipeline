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

package de.phoenix.argument;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.InvalidParameterException;

import org.junit.Test;

import de.phoenix.submissionpipeline.ArgumentHandler;

public class ArgumentTest {

    @Test
    public void testSingleValues() {
        ArgumentHandler handler = new ArgumentHandler(new String[]{"-c", "MyHelloWorld.java", "-t", "HelloWorldTest.java", "-l", "Test.jar"});
        assertEquals("[Test.jar]", handler.getLibraries().toString());
        assertEquals("[MyHelloWorld.java]", handler.getClassFiles().toString());
        assertEquals("[HelloWorldTest.java]", handler.getTestFiles().toString());
    }

    @Test
    public void testMultipleValues() {
        ArgumentHandler handler = new ArgumentHandler(new String[]{"-c", "HelloWorld.java", "MyHelloWorld.java", "-t", "HelloWorldTest.java", "SecondTest.java", "-l", "Test.jar", "Library.jar"});
        assertEquals("[Test.jar, Library.jar]", handler.getLibraries().toString());
        assertEquals("[HelloWorld.java, MyHelloWorld.java]", handler.getClassFiles().toString());
        assertEquals("[HelloWorldTest.java, SecondTest.java]", handler.getTestFiles().toString());
    }

    @Test
    public void testOnlyClasses() {
        ArgumentHandler handler = new ArgumentHandler(new String[]{"-c", "HelloWorld.java", "MyHelloWorld.java"});

        assertEquals(0, handler.getLibraries().size());
        assertEquals("[HelloWorld.java, MyHelloWorld.java]", handler.getClassFiles().toString());
        assertEquals(0, handler.getTestFiles().size());
    }

    @Test
    public void testOnlyLibraries() {
        ArgumentHandler handler = new ArgumentHandler(new String[]{"-l", "Test.jar", "Library.jar"});
        assertEquals("[Test.jar, Library.jar]", handler.getLibraries().toString());
        assertEquals(0, handler.getClassFiles().size());
        assertEquals(0, handler.getTestFiles().size());
    }

    @Test(expected = InvalidParameterException.class)
    public void testEmptyList() {
        ArgumentHandler handler = new ArgumentHandler(new String[]{});
        assertNull(handler);
    }

}
