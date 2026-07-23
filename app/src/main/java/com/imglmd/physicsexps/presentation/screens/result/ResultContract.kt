package com.imglmd.physicsexps.presentation.screens.result

import android.net.Uri
import com.imglmd.physicsexps.core.network.OnlineState
import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.experiments.model.ExperimentResult

object ResultContract {

    sealed interface State {
        data object Loading: State
        data class Success(
            val result: ExperimentResult,
            val onlineState: OnlineState = OnlineState(),
            val comments: List<Comment> = emptyList(),
            val media: List<Media> = emptyList(),
            val isSaved: Boolean = true,
            val isSaving: Boolean = false,
            val isMediaLoading: Boolean = false,
            val isMediaUploading: Boolean = false,
            val mediaErrorMessage: String? = null
        ): State
        data class Error(val message: String): State
    }

    sealed interface Intent {
        data object Back: Intent
        data object Save: Intent
        data object Delete: Intent
        data object Change: Intent
        data object Compare: Intent
        data object OpenChart: Intent
        data object OpenSolution: Intent
        data object RefreshMedia: Intent

        data class AddComment(val text: String): Intent
        data class DeleteComment(val id: Int): Intent
        data class UploadMedia(val uri: Uri): Intent
        data class DeleteMedia(val mediaId: String): Intent
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object NavigateHome : Effect
        data class NavigateExperiment(
            val id: String,
            val inputs: Map<String, String>,
            val replaceRunId: Int? = null
        ) : Effect
        data class NavigateCompare(val runId: Int) : Effect
        data class NavigateChart(val runId: Int) : Effect
        data object NavigateSolution : Effect
    }
}
