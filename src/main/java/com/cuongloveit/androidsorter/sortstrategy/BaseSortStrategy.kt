package com.cuongloveit.androidsorter.sortstrategy

import org.jetbrains.kotlin.psi.KtDeclaration


abstract class BaseSortStrategy(private val mAllDeclarations: List<KtDeclaration>) {

    protected var mOrdering: List<String>? = null
    fun sort(): List<KtDeclaration> {
        if (mOrdering == null) {
            return mAllDeclarations
        }
        mAllDeclarations.sortedWith(Comparator.comparingInt<KtDeclaration> { o -> mOrdering!!.indexOf(o.javaClass.name) })


       mAllDeclarations.sortedWith(kotlin.Comparator { o1, o2 ->
           mOrdering!!.indexOf(o1.javaClass.name)- mOrdering!!.indexOf(o2.javaClass.name)
       })

        return mAllDeclarations
    }
}
