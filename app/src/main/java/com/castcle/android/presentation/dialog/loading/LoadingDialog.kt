package com.castcle.android.presentation.dialog.loading

import com.castcle.android.core.base.dialog.BaseDialog
import com.castcle.android.databinding.DialogLoadingBinding

class LoadingDialog(binding: DialogLoadingBinding) :
    BaseDialog(binding = binding, isCancelable = false, width = 1.0, height = 1.0)