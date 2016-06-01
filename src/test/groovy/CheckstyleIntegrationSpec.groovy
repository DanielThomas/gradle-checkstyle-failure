import com.google.common.io.Files
import nebula.test.IntegrationSpec

class CheckstyleIntegrationSpec extends IntegrationSpec {
    def 'task runs successfully'() {
        setup:
        buildFile << '''
apply plugin: 'java'
apply plugin: 'checkstyle'

repositories {
    mavenCentral()
}

tasks.withType(Checkstyle) {
    ignoreFailures = true
}

dependencies {
    testCompile 'junit:junit:4.11'
}
'''
        javaFile()
        testFile()

        when:
        def result = runTasksSuccessfully('clean', 'check')

        then:
        result.wasExecuted(':checkstyleMain')
        result.wasExecuted(':checkstyleTest')

        new File(projectDir, 'build/checkstyle/checkstyle.xml').exists()
        new File(projectDir, 'build/reports/checkstyle/main.xml').exists()
        new File(projectDir, 'build/reports/checkstyle/test.xml').exists()
    }

    def void javaFile() {
        File javaFile = new File(projectDir, "src/main/java/netflix/Hello.java")
        Files.createParentDirs(javaFile)
        javaFile.text = '''
package netflix;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
'''
    }

    def void testFile() {
        File testFile = new File(projectDir, "src/test/java/netflix/HelloTest.java")
        Files.createParentDirs(testFile)
        testFile.text = '''
package netflix;

import org.junit.Test;

public class HelloTest {
    @Test
    public void testSomething() {
        System.out.println("Nothing to test");
    }
}
'''
    }
}
