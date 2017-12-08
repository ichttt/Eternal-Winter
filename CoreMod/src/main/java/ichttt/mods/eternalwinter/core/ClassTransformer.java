package ichttt.mods.eternalwinter.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Transforms SPacketChunkData to only send snow biomes
 */
public class ClassTransformer implements IClassTransformer, Opcodes {

    public static TransformerState STATE = TransformerState.IDLE;
    static final Logger logger = LogManager.getLogger("EternalWinter Core");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || !transformedName.equals("net.minecraft.network.play.server.SPacketChunkData"))
            return basicClass;
        try {
            SetupHook.lateSetup(); //classes should be loaded by now
        } catch (ClassCircularityError error) {
            logger.fatal("Failed to do late init! Required classes could not be loaded");
            throw error;
        } catch (RuntimeException | Error throwable) {
            logger.fatal("Failed to do late init! Cause: see below");
            logger.catching(throwable);
            throw throwable;
        }
        logger.info("Found correct class " + name + ", searching method");
        STATE = TransformerState.TRANSFORMING;
        ClassNode node = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(node, 0);
        for (MethodNode method : node.methods) {
            if (method.name.equals("extractChunkData") || method.name.equals("func_189555_a"))  {
                logger.info("Found correct method, searching instruction");
                if (handleCorrectMethodNode(method)) {
                    logger.info("Patch seems to be successful!");
                    break;
                } else {
                    logger.fatal("Patch failed!");
                    STATE = TransformerState.ERROR;
                    throw new RuntimeException("Could not transform critical class" + transformedName);
                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        byte[] bytes = writer.toByteArray();
        STATE = TransformerState.TRANSFORMED;
        return bytes;
    }

    private static boolean handleCorrectMethodNode(MethodNode method) {
        for (int i = 0; i < method.instructions.size(); i++) {
            AbstractInsnNode node = method.instructions.get(i);
            if (node.getOpcode() == INVOKEVIRTUAL) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                if ((methodNode.name.equals("getBiomeArray") || methodNode.name.equals("func_76605_m")) && methodNode.desc.equals("()[B")) {
                    logger.info("Found correct node " + node + " , validating...");
                    AbstractInsnNode aload1 = methodNode.getPrevious().getPrevious();
                    if (aload1.getOpcode() != ALOAD || ((VarInsnNode) aload1).var != 1) {
                        logger.fatal("Sanity check failed: Did not find ALOAD1");
                        if (aload1.getOpcode() != ALOAD)
                            logger.fatal("Got Opcode " + aload1.getOpcode() + ", expected Opcode " + ALOAD);
                        else
                            logger.fatal("Got var " + ((VarInsnNode) aload1).var + ", expected var 1");
                        return false;
                    }
                    logger.info("Validation successful, patching...");
                    method.instructions.remove(methodNode.getPrevious());
                    method.instructions.insertBefore(methodNode.getNext(), new FieldInsnNode(GETSTATIC, SetupHook.class.getName().replaceAll("\\.", "/"), "toSend", "[B"));
                    method.instructions.remove(methodNode);
                    return true;
                }
            }
        }
        logger.fatal("Could not find the needle in the haystack!");
        return false;
    }
}
