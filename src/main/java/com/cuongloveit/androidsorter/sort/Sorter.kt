package com.cuongloveit.androidsorter.sort

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.JavaPsiFacade
import com.cuongloveit.androidsorter.action.Constant
import com.cuongloveit.androidsorter.sortstrategy.CommonSortStrategy
import org.jetbrains.kotlin.psi.KtClassOrObject


/**
 * Created by XQ Yang on 9/21/2018  5:06 PM.
 * Description :
 */

class Sorter(private val mPsiClass: KtClassOrObject) {

    fun sort() {
        realSort(mPsiClass)
    }


    fun realSort(classOrObject: KtClassOrObject) {
        val declarations = classOrObject.declarations
        val before = declarations.hashCode()

        val needAddDeclaredComment = !classOrObject.text.contains(Constant.DECLARED_FUNCTIONS)
        val needOverrideComment = !classOrObject.text.contains(Constant.DECLARED_FUNCTIONS)

        val sortedData = CommonSortStrategy(declarations).sort(classOrObject)
        val sortedList = sortedData.sortedList
        val after = sortedList.hashCode()
        sortedList.forEach {
            if (it is KtClassOrObject) {
                realSort(it)
            }
        }
        if (before != after) {
            sortedList.forEachIndexed { index, ktDeclaration ->
                classOrObject.addDeclaration(ktDeclaration)


            }
            declarations.forEach {
                it.delete()
            }
        }


        val editor = FileEditorManager.getInstance(classOrObject.project).selectedTextEditor
        if (needAddDeclaredComment) {
            val comment = JavaPsiFacade.getElementFactory(classOrObject.project).createCommentFromText("/*DECLARED_FUNCTIONS*/", classOrObject)
            //classOrObject.addAfter(comment, classOrObject.findFunctionByName())

            //editor?.document?.insertString(editor.document.text.indexOf("override"), "\n//------------Declared Functions----------\n")
        }

        if (needOverrideComment) {
           // editor?.document?.insertString(editor.document.text.indexOf("override"), "\n//------------Override Functions----------\n")
        }

    }

}
