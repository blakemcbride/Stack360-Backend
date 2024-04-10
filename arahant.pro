# ProGuard configuration file to process the contents of arahant.war.

-injars  Arahant/WEB-INF/classes
-outjars Secure/WEB-INF/classes

-libraryjars Arahant/WEB-INF/lib/abcl.jar
-libraryjars libs(!1.0/**)
-libraryjars ../lib-extra/servlet-api.jar

# Under Mac
#-libraryjars /System/Library/Frameworks/JavaVM.framework/Classes/classes.jar
#-libraryjars /System/Library/Frameworks/JavaVM.framework/Classes/jsse.jar
#-libraryjars /System/Library/Frameworks/JavaVM.framework/Classes/jce.jar

# Under Linux or Windows
-libraryjars <java.home>/lib/rt.jar
-libraryjars <java.home>/lib/jsse.jar
-libraryjars <java.home>/lib/jce.jar


# Note that we've put a filter (!1.0/**) on the class files in the libraries,
# to ignore some classes that are strangely packaged but that we don't need
# anyway.

# We can get away with not specifying library jars if we don't optimize or
# preverify, but results are much better if we do.
# -dontoptimize
# -dontpreverify

# We can suppress all warnings, but it's better to at least let ProGuard print
# them out and then still ignore them. The messages generally point in the
# direction of problems that we should solve.
# -dontwarn
-ignorewarnings

# We can suppress all notes, since these are just suggestions from ProGuard.
-dontnote

# It can be nice to get some progress messages and statistics on the results.
-verbose

# The obfuscation mapping file will help us de-obfuscating stack traces in
# bug reports.
-printmapping SecureArahant.map

# Keep some minimal debug information for useful stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# If the code relies on annotations, we have to keep them.
-keepattributes *Annotation*

# Some libraries and frameworks require metadata for introspection.
-keepattributes InnerClasses,EnclosingMethod,Signature

# Some libraries even require metadata about exceptions.
-keepattributes Exceptions

# Make sure fields and methods with the @Resource annotation are
# not removed or renamed.
-keepclassmembers class * {
 @javax.annotation.Resource <fields>;
 @javax.annotation.Resource <methods>;
}

# New for NetBeans 7.1
# Make sure classes, fields, getters and setters of classes with
# the @XmlType annotation are not removed or renamed.
-keep @javax.xml.bind.annotation.XmlType class * {
    <fields>;
#    void set*(***);
#    void set*(int, ***);
#    boolean is*(); 
#    boolean is*(int);
#    *** get*();
#    *** get*(int);
}

# Now, for the essential -keep options.
# We'll play on the safe side, preserving all public classes and class members
# (as if we're processing a library).
-keep public class * {
#  public protected *;
  public *;
}

# If some package visible classes might be accessed by introspection, we can
# keep all classes and their names, but that reduces the amount of obfuscation.
-keep class *


# Finally, we're keeping some standard elements, as explained in the ProGuard
# manual > Examples.

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
