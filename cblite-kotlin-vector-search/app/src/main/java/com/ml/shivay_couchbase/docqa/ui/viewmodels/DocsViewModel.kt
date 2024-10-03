package com.ml.shivay_couchbase.docqa.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.ml.shivay_couchbase.docqa.domain.DocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocsViewModel @Inject constructor(val documentsUseCase: DocumentsUseCase) : ViewModel() {

    val documentsFlow = documentsUseCase.getAllDocuments()
}
