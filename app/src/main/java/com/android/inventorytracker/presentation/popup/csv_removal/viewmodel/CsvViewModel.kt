package com.android.inventorytracker.presentation.popup.csv_removal.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CSV (val id: Int, val subunit: Int)

@HiltViewModel
class CsvViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    application: Application
) : AndroidViewModel(application) {

    val model = itemRepository.observeItemModels()

    private val _data = MutableLiveData<List<CSV>>()
    val data: LiveData<List<CSV>> = _data

    private val _droppedCount = MutableLiveData<Int>()
    val droppedCount: LiveData<Int> = _droppedCount


    fun loadCsv(csvUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            var dropped = 0
            val validIds = itemRepository.observeItemModels()
                .first()
                .map { it.item.id }
                .toSet()
            val parsedList = context.contentResolver.openInputStream(csvUri)
                ?.bufferedReader()
                ?.useLines { lines ->
                    lines.drop(1)
                        .mapNotNull { line ->
                            val tokens = line.split(",")
                            val id = tokens.getOrNull(0)?.toIntOrNull()
                            val subunit = tokens.getOrNull(1)?.toIntOrNull()
                            if (id != null && subunit != null) {
                                if (!validIds.contains(id)) {
                                    dropped++
                                    return@mapNotNull null
                                }
                                if (subunit <= 0) {
                                    dropped++
                                    return@mapNotNull null
                                }
                                CSV(id, subunit)
                            } else {
                                dropped++
                                null
                            }
                        }
                        .groupBy { it.id }
                        .map { (id, items) ->
                            val totalSubunit = items.sumOf { it.subunit }
                            CSV(id, totalSubunit)
                        }
                } ?: emptyList()
            _droppedCount.postValue(dropped)
            _data.postValue(parsedList)
        }
    }

    fun addData(id: Int) {
        val currentList = _data.value ?: return
        val updatedList = currentList.toMutableList().apply {
            add(CSV(id, 0))
        }
        _data.value = updatedList
    }

    fun removeData(id: Int) {
        val currentList = _data.value ?: return
        val updatedList = currentList.toMutableList().apply {
            val index = indexOfFirst { it.id == id }
            if (index != -1) {
                removeAt(index)
            }
        }
        _data.value = updatedList
    }
    fun clearData() {
        _data.value = emptyList()
    }
    fun updateData(csv: CSV) {
        val currentList = _data.value ?: return
        val updatedList = currentList.toMutableList().apply {
            val index = indexOfFirst { it.id == csv.id }
            if (index != -1) {
                set(index, csv)
            }
        }
        _data.value = updatedList
    }
}
