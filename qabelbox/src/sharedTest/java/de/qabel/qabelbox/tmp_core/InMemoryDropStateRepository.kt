package de.qabel.qabelbox.tmp_core

import de.qabel.core.drop.DropURL
import de.qabel.core.repository.DropStateRepository
import de.qabel.core.repository.entities.DropState
import de.qabel.core.repository.exception.EntityNotFoundException
import de.qabel.core.repository.exception.PersistenceException

import java.util.HashMap

@Deprecated("Replace with core")
class InMemoryDropStateRepository : DropStateRepository {

    private val states = HashMap<String, DropState>()

    @Throws(EntityNotFoundException::class, PersistenceException::class)
    override fun getDropState(drop: String): String {
        if (!states.containsKey(drop)) {
            throw EntityNotFoundException("drop not found: " + drop)
        }
        return states[drop]?.eTag ?: throw EntityNotFoundException("DropState not found")
    }

    @Throws(PersistenceException::class)
    override fun setDropState(drop: String, state: String) {
        states.put(drop, DropState(drop, state))
    }

    override fun getDropState(dropUrl: DropURL): DropState {
        return states.getOrDefault(dropUrl.toString(), DropState(dropUrl.toString(), ""))
    }

    override fun setDropState(dropState: DropState) {
        states.put(dropState.drop, dropState)
    }

    fun <K, V> HashMap<K, V>.getOrDefault(key: K, default: V): V {
        if (containsKey(key)) {
            return get(key)!!
        }
        return default
    }
}
