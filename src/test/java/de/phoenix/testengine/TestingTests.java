package de.phoenix.testengine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.phoenix.submissionpipeline.compiler.CharSequenceCompiler;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompilerException;
import de.phoenix.util.TextFileReader;

public class TestingTests {

    private static TextFileReader reader;

    @BeforeClass
    public static void beforeClass() {
        reader = new TextFileReader();
    }

    private Class<Object> compileJavaFile(CharSequenceCompiler<Object> compiler, String className, InputStream resource) throws ClassCastException, CharSequenceCompilerException, CharacterCodingException {

        String source = reader.read(resource);
        return compiler.compile(className, source);
    }

    @Test
    public void runStaticTest() throws ClassCastException, CharSequenceCompilerException, CharacterCodingException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> builderClass = compileJavaFile(com, "Builder", getClass().getResourceAsStream("/testClasses/builder/goodImpl/Builder.java"));
        assertNotNull(builderClass);
        Class<Object> testClass = compileJavaFile(com, "BuilderTest", getClass().getResourceAsStream("/testClasses/builder/BuilderTest.java"));
        assertNotNull(testClass);

        assertEquals(0, JUnitCore.runClasses(testClass).getFailureCount());
    }

    @Test(timeout = 2000)
    public void runEndlessLoop() throws ClassCastException, CharSequenceCompilerException, CharacterCodingException {
        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();
        Class<Object> counterClass = compileJavaFile(com, "Counter", getClass().getResourceAsStream("/testClasses/counter/infiniteImpl/Counter.java"));
        assertNotNull(counterClass);

        Class<Object> testClass = compileJavaFile(com, "CounterTest", getClass().getResourceAsStream("/testClasses/counter/CounterTest.java"));
        assertNotNull(testClass);

        Result result = JUnitCore.runClasses(testClass);
        assertNotNull(result);
        assertEquals("test(CounterTest): test timed out after 500 milliseconds", result.getFailures().get(0).toString());
    }

    @Test
    public void sameClassnamesDifferentImplementation() throws ClassCastException, CharSequenceCompilerException, CharacterCodingException {

        CharSequenceCompiler<Object> com = new CharSequenceCompiler<Object>();

        String correctSource = reader.read(getClass().getResourceAsStream("/testClasses/counter/goodImpl/Counter.java"));

        // Correct class compiling and testing

        // Load correct working source
        // Compile sources
        Class<Object> correctClass = com.compile("Counter", correctSource);
        Class<Object> testClass = compileJavaFile(com, "CounterTest", getClass().getResourceAsStream("/testClasses/counter/CounterTest.java"));
        assertNotNull(correctClass);

        Result result = JUnitCore.runClasses(testClass);

        assertNotNull(result);
        assertEquals(0, result.getFailureCount());

        // Unload the current compiled classes
        com = null;
        System.gc();

        // Test wrong class after compiling and testing correctly

        com = new CharSequenceCompiler<Object>();
        // Modify to be wrong
        String wrongSource = correctSource.replaceAll(Pattern.quote("++counter"), "--counter");
        Class<Object> wrongClass = com.compile("Counter", wrongSource);
        testClass = compileJavaFile(com, "CounterTest", getClass().getResourceAsStream("/testClasses/counter/CounterTest.java"));
        assertNotNull(wrongClass);

        result = JUnitCore.runClasses(testClass);
        assertEquals(1, result.getFailureCount());
        assertEquals("test(CounterTest): expected:<Counter: []2> but was:<Counter: [-]2>", result.getFailures().get(0).toString());
    }
}
