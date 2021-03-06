package de.phoenix.testengine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.CharacterCodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.phoenix.submissionpipeline.compiler.CharSequenceCompiler;
import de.phoenix.submissionpipeline.compiler.CharSequenceCompilerException;
import de.phoenix.util.TextFileReader;

public class CompilerTest {

    private static TextFileReader reader;

    @BeforeClass
    public static void beforeClass() {
        reader = new TextFileReader();
    }

    @Test
    public void counterClassTest() throws ClassCastException, CharSequenceCompilerException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CharacterCodingException {
        CharSequenceCompiler<Object> t = new CharSequenceCompiler<Object>();
        assertNotNull(t);
        String javaSource = reader.read(getClass().getResourceAsStream("/testClasses/counter/goodImpl/Counter.java"));
        Class<?> clazz = t.compile("Counter", javaSource);
        assertNotNull(clazz);
        assertEquals("Counter", clazz.getName());

        Constructor<?> standardConstructor = clazz.getConstructor();
        assertNotNull(standardConstructor);

        Object counterObject = standardConstructor.newInstance();
        assertNotNull(counterObject);

        Method countMethod = clazz.getMethod("count");
        countMethod.invoke(counterObject);
        countMethod.invoke(counterObject);

        Method toStringMethod = clazz.getMethod("toString");
        assertEquals("Counter: 2", toStringMethod.invoke(counterObject));
    }

    @Test
    public void builderClassTest() throws ClassCastException, CharSequenceCompilerException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CharacterCodingException {

        CharSequenceCompiler<Object> t = new CharSequenceCompiler<Object>();
        assertNotNull(t);

        String javaSource = reader.read(getClass().getResourceAsStream("/testClasses/builder/goodImpl/Builder.java"));
        Class<?> clazz = t.compile("Builder", javaSource);

        assertNotNull(clazz);
        assertEquals("Builder", clazz.getName());

        Constructor<?> standardConstructor = clazz.getConstructor();
        assertNotNull(standardConstructor);

        Object builderObject = standardConstructor.newInstance();
        assertNotNull(builderObject);

        Method appendMethod = clazz.getMethod("append", String.class);
        appendMethod.invoke(builderObject, "Hello ");
        appendMethod.invoke(builderObject, "World");

        Method toStringMethod = clazz.getMethod("toString");
        assertEquals("Hello World", toStringMethod.invoke(builderObject));
    }

    @Test
    public void multipleClassTest() throws CharSequenceCompilerException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

        // Simple interface with one method
        String s = "public interface MyPredicate {  public boolean accept(int i);   }";
        // Implementation for even numbers
        String s2 = "public class EvenPredicate implements MyPredicate { public boolean accept(int i) { return i%2==0;}}";

        Map<String, CharSequence> classesToCompile = new LinkedHashMap<String, CharSequence>();

        // put them in wrong order
        classesToCompile.put("EvenPredicate", s2);
        classesToCompile.put("MyPredicate", s);

        // Compile them
        CharSequenceCompiler<Object> compiler = new CharSequenceCompiler<Object>();
        Map<String, Class<Object>> compileClasses = compiler.compile(classesToCompile);

        // Invoke accept method
        Class<Object> implementationClass = compileClasses.get("EvenPredicate");
        Object object = implementationClass.newInstance();
        Method method = implementationClass.getMethod("accept", int.class);
        boolean res = (Boolean) method.invoke(object, 2);
        assertTrue(res);

        Class<Object> interfaceClass = compileClasses.get("MyPredicate");

        try {
            interfaceClass.newInstance();
            fail("Can't instantiate an interface!");
        } catch (InstantiationException e) {

        }
    }

    @Test
    public void packageCompilingTest() throws CharSequenceCompilerException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, CharacterCodingException {
        CharSequenceCompiler<Object> compiler = new CharSequenceCompiler<Object>();

        Map<String, CharSequence> classesToCompile = new LinkedHashMap<String, CharSequence>();

        String utilSource = reader.read(getClass().getResourceAsStream("/testClasses/other/util/Util.java"));

        String helloSource = reader.read(getClass().getResourceAsStream("/testClasses/other/HelloWorld.java"));
        // Use not qualified class names
        classesToCompile.put("Util", utilSource);
        classesToCompile.put("HelloWorld", helloSource);
        Map<String, Class<Object>> compileClasses = null;;

        try {
            compileClasses = compiler.compile(classesToCompile);
        } catch (CharSequenceCompilerException e) {
            e.printStackTrace();
            System.out.println(e.getDiagnostics().getDiagnostics());;
        }

        Class<Object> helloWorldClass = compileClasses.get("HelloWorld");
        Object helloWorldObject = helloWorldClass.newInstance();
        Method sayHi = helloWorldClass.getMethod("sayHi");
        assertEquals("Hello World", sayHi.invoke(helloWorldObject).toString());
    }
}
