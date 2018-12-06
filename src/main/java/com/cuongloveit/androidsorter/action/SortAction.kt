package com.cuongloveit.androidsorter.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiJavaParserFacade
import com.cuongloveit.androidsorter.sort.Sorter
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade
import java.util.*


/**
 * Created by XQ Yang on 9/21/2018  5:06 PM.
 * Description :
 */

class SortAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {

        val psiClass = getPsiClassFromContext(e)

        if (psiClass != null) {
            startSort(psiClass)
        }


    }

    override fun update(e: AnActionEvent?) {
        super.update(e)

    }

    private fun startSort(psiClass: List<KtClassOrObject>) {
        for (aClass in psiClass) {
            object : WriteCommandAction.Simple<Any>(aClass.project, aClass.containingFile) {
                @Throws(Throwable::class)
                override fun run() {
                    Sorter(aClass).sort()
                }
            }.execute()
        }
    }

    /**
     * @param e the action event that occurred
     * @return The PSIClass object based on which class your mouse cursor was in
     */
    protected fun getPsiClassFromContext(e: AnActionEvent): List<KtClassOrObject>? {
        val psiFile = e.getData(LangDataKeys.PSI_FILE) ?: return null

        val children = psiFile.children
        val result = ArrayList<KtClassOrObject>()
        for (child in children) {
            if (child is KtClassOrObject) {
                result.add(child)
            }
        }
        return result
    }
}
