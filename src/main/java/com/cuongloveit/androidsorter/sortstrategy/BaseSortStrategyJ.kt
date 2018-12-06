package com.cuongloveit.androidsorter.sortstrategy

import com.cuongloveit.androidsorter.action.SortedData
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty

abstract class BaseSortStrategyJ(private val mAllDeclarations: List<KtDeclaration>) {
    protected var mOrdering: ArrayList<String>? = null

    fun sort(classOrObject: KtClassOrObject): SortedData {
        var indexOverrideMethods = -1
        var indexDeclaredMethods = -1
        //group declaration
        val allNewDeclaration = ArrayList<KtDeclaration>()
        val group = mAllDeclarations.groupBy { it.javaClass.name }

        //sort properties
        var variableList = group[KtProperty::class.java.name]
        variableList = variableList?.sortedWith(compareBy { it.name })
        variableList?.let { allNewDeclaration.addAll(it) }

        //sort method
        val methods = ArrayList(group[KtNamedFunction::class.java.name])
        val overrideMethods = methods.filter { it.text.startsWith("override") }
        methods.removeAll(overrideMethods)
        allNewDeclaration.addAll(overrideMethods)// add override methods to first

        if (allNewDeclaration.isNotEmpty() && methods.isNotEmpty()) {
            indexDeclaredMethods = allNewDeclaration.size
        }
        methods.sortWith(compareBy { it.name })
        allNewDeclaration.addAll(methods)





        group.forEach { key, subList ->
            if (key != KtProperty::class.java.name && key != KtNamedFunction::class.java.name)
                allNewDeclaration.addAll(subList)
        }

        var startOffsetDeclaredMethods = -1
        var startOffsetOverrideMethods = -1
        if (indexDeclaredMethods != -1) {
            startOffsetDeclaredMethods = allNewDeclaration[indexDeclaredMethods].startOffsetInParent
        }
        if (indexOverrideMethods != -1) {
            startOffsetOverrideMethods = allNewDeclaration[indexOverrideMethods].startOffsetInParent
        }

        return SortedData(sortedList = allNewDeclaration,
                startOffsetDeclaredMethods = startOffsetDeclaredMethods,
                startOffsetOverrideMethods = startOffsetOverrideMethods)

    }

}
