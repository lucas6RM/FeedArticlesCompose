package com.mercierlucas.feedarticlescompose.ui.custom

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_ALL
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT

@Composable
fun RadioButtonSingleSelectionHorizontal(
    currentCategory: Int = CATEGORY_ALL,
    modifier: Modifier = Modifier,
    radioOptions : List<Int>,
    callbackRBSelected:(Int) ->  Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {

    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(currentCategory) }
    var text = ""

    Row(modifier.selectableGroup(),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        radioOptions.forEach { index ->
            Column(
                Modifier
                    .selectable(
                        selected = (index == selectedOption),
                        onClick = {
                            onOptionSelected(index)
                            callbackRBSelected.invoke(index)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    RadioButton(
                        selected = (index == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary),
                        interactionSource = interactionSource
                    )
                    when(index){
                        CATEGORY_SPORT -> text = stringResource(id = R.string.sport)
                        CATEGORY_MANGA -> text = stringResource(id = R.string.manga)
                        CATEGORY_OTHERS -> text = stringResource(id = R.string.others)
                        CATEGORY_ALL -> text = stringResource(id = R.string.all)
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun RadioButtonSingleSelectionPreview() {
    RadioButtonSingleSelectionHorizontal(CATEGORY_ALL,radioOptions = listOf(0,1,2,3), callbackRBSelected = {})
}