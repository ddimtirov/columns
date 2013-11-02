import org.objectweb.asm.util.ASMifierClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

/**
 * <p><small>
 * Initial author: <a href="mailto:dimitar.dimitrov@gmail.com">ddimitrov</a>,
 * Created: May 18, 2009 3:52:53 PM
 * <small></p>
 */
public class AsmPlayground {
    Collection c = new ArrayList();

    public boolean foo(Object o) {
        Date date = (Date) o;

        return c.add(date);
    }

    public void a( MethodVisitor mv) {
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, "java/util/Date");
        mv.visitVarInsn(ASTORE, 2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "AsmPlayground", "c", "Ljava/util/Collection;");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "add", "(Ljava/lang/Object;)Z");
        mv.visitInsn(IRETURN);
        mv.visitMaxs(2, 3);
        mv.visitEnd();

    }
    public static void main(String[] args) throws IOException {
        ClassReader reader = new ClassReader(AsmPlayground.class.getResourceAsStream("AsmPlayground.class"));
        reader.accept(new ASMifierClassVisitor(new PrintWriter(System.out)), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }
}
