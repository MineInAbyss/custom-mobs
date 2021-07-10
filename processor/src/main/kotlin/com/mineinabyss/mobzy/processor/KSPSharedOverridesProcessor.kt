//package com.mineinabyss.mobzy.processor
//
//import com.google.devtools.ksp.processing.*
//import com.google.devtools.ksp.symbol.KSAnnotated
//import com.google.devtools.ksp.symbol.KSClassDeclaration
//import com.google.devtools.ksp.validate
//import com.google.devtools.ksp.visitor.KSDefaultVisitor
//import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
//import javax.lang.model.element.TypeElement
//import javax.lang.model.type.DeclaredType
//import kotlin.reflect.KClass
//
//@Retention(AnnotationRetention.SOURCE)
//annotation class GenerateFromBase(val base: KClass<*>, val createFor: Array<KClass<*>>)
//
//@KotlinPoetMetadataPreview
//@ExperimentalStdlibApi
//class SharedOverridesProcessor(
//    val codeGenerator: CodeGenerator,
//    val logger: KSPLogger
//) : SymbolProcessor {
//
////    private val elements: Elements by lazy { processingEnv.elementUtils }
////    private val types: Types by lazy { processingEnv.typeUtils }
////    private val classInspector by lazy { ElementsClassInspector.create(elements, types) }
//
//    override fun process(resolver: Resolver): List<KSAnnotated> {
//        logger.warn("Hello world!")
//        val symbols = resolver.getSymbolsWithAnnotation(GenerateFromBase::class.java.name)
//        symbols
//            .filterIsInstance<KSClassDeclaration>()
//            .forEach {  }
//        return symbols.filter { !it.validate() }.toList()
//    }
//
//    inner class SharedOverridesVisitor: KSDefaultVisitor
//
////    private fun createTargetsFromBase(baseElement: TypeElement, targetMirror: TypeMirror) {
////        val baseSpec = baseElement.toTypeSpec(classInspector)
////        val targetElement = (targetMirror as DeclaredType).asElement()
////
////        val packageName = baseElement.packageName
////        val fileName = "Mobzy${targetElement.simpleName}"
////        val fileBuilder = FileSpec.builder(packageName, fileName)
////        println("ProcessingEnv: $processingEnv")
////        val generatedDirectory = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]!!
////        val sourceLines = baseElement.readSource(File(generatedDirectory))
////
////        //create class
////        val newClass = TypeSpec.classBuilder(fileName)
////            .superclass(targetMirror.asTypeName())
////            .primaryConstructor(
////                FunSpec.constructorBuilder()
////                    .addParameter("world", NMSWorld::class)
////                    .addParameter("type", typeOf<NMSEntityType<*>>().asTypeName())
////                    .build()
////            )
////            //https://github.com/square/kotlinpoet#spaces-wrap-by-default
////            .addSuperclassConstructorParameter("type as EntityTypes<$targetMirror>, world".replace(' ', '·'))
////            .addSuperinterfaces(baseSpec.superinterfaces.map { it.key })
////            .addModifiers(KModifier.ABSTRACT)
////
////        fileBuilder.addType(newClass.build())
////
////        //build to file
////        val file = fileBuilder.build().toJavaFileObject()
////        val outputLines = file.openReader(true).readLines().toMutableList()
////
////        //add imports
////        val existingImports = outputLines.filter { it.startsWith("import") }
////        outputLines.addAll(1, sourceLines.filter { it.startsWith("import") && !existingImports.contains(it) })
////
////        //copy code within the base class
////        val start = sourceLines.indexOfFirst { it.contains("class ${baseElement.simpleName}") }
////        val innerCode = sourceLines.drop(start + 1)
////        val classLines = innerCode.takeWhile { line -> !line.startsWith('}') }
////
////        outputLines[outputLines.lastIndex] = outputLines[outputLines.lastIndex] + " {"
////        outputLines.addAll(classLines + "}")
////        //write file
////        val writeTo = File(generatedDirectory, file.toUri().toString())
////        writeTo.parentFile.mkdirs()
////        writeTo.writeText(outputLines.joinToString(separator = "\n"))
////    }
////
////    private val TypeElement.packageName get() = processingEnv.elementUtils.getPackageOf(this).qualifiedName.toString()
//}
//
//class SharedOverridesProcessorProvider : SymbolProcessorProvider {
//    @ExperimentalStdlibApi
//    @KotlinPoetMetadataPreview
//    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
//        return SharedOverridesProcessor(environment.codeGenerator, environment.logger)
//    }
//}
