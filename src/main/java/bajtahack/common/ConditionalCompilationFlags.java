package bajtahack.common;

/**
 * Static final variables are know in compile time.Therefore the code inside if like this :
 * <pre>
 * <code>
 *    if (STATIC_FINAL_FALSE) callSomeMethod();
 * </code>
 * </pre>
 * does not get included into compiled bytecode, beacuse the value of static final variables is known in compile time.
 * 
 * This patter is known also as java conditional compilation. Taken from book Hardcore Java. 
 * 
 * @author <a href="mailto:gustinmi@gmail.com">Mitja Guštin</a>
 *
 */
public class ConditionalCompilationFlags {
    
    public static final boolean USE_GOOGLE_SPEACH = true;

}
