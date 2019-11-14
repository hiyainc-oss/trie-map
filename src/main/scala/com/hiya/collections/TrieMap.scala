package com.hiya.collections

import scala.annotation.tailrec
import scala.collection.immutable.HashMap

/**
 * A TrieMap which can contain values associated with string keys,
 * which are stored in a prefix tree, making prefix searches efficient
 *
 * The TrieMap is a recursive data structure
 * You can create one by calling empty, or build.
 * @param value The value stored in the Map
 * @param children The children in the TrieMap
 * @tparam K The type of the key stored in the TrieMap
 * @tparam V The type of the value stored in the TrieMap
 */
final case class TrieMap[K, V](value: Option[V], children: HashMap[K, TrieMap[K, V]]) {
  /**
   * @return returns a prettified version of the TrieMap
   */
  override def toString: String = TrieMap.dump(this)

  /**
   * Inserts a key and value association into a TrieMap
   * @param key The key
   * @param value The value
   * @return The TrieMap with the inserted key - value entry
   */
  def insert(key: Seq[K], value: V): TrieMap[K, V] = TrieMap.insert(this, key, value)

  /**
   * Finds the longest prefix associated with the key inputted in the TrieMap
   * @param key The String key which needs to be found
   * @return The value associated with the longest prefix of the input key, None if no prefix found
   */
  def findPrefixFor(key: Seq[K]): Option[V] = TrieMap.findPrefixFor(this, key.toList)

  /**
   * Finds if there's a prefix found in the TrieMap for the input key
   * @param key The String input key needs to be found
   * @return true if found, false otherwise
   */
  def containsPrefixFor(key: Seq[K]): Boolean = TrieMap.findPrefixFor(this, key.toList).nonEmpty

  /**
   * @return A prettified version of the TrieMap, the following code:
   *         val map = TrieMap.empty[Int]
   *                     .insert("pre", 1)
   *                     .insert("another", 2)
   *         println(map.dump)
   *             outputs:
   *          """:
   *          a -> :
   *          	n -> :
   *          		o -> :
   *          			t -> :
   *          				h -> :
   *          					e -> :
   *          						r -> 2:
   *          p -> :
   *          	r -> :
   *          		e -> 1: """
   *
   */
  def dump: String = TrieMap.dump(this)


  /**
   * A method which converts the TrieMap to a scala collection Map by concatenating the keys together.
   * Useful for serialization.
   * @return The Map containing the keys, concatenated.
   */
  def toMap: Map[Seq[K], V] = TrieMap.toMap(this)
}

object TrieMap {

  /**
   * Creates an empty TrieMap
   * @tparam K The type of the keys stored in it
   * @tparam V The type of the values stored in it
   * @return The empty TrieMap
   */
  def empty[K, V]: TrieMap[K, V] = TrieMap[K, V](None, HashMap.empty[K, TrieMap[K, V]])

  /**
   * Builds a TrieMap from a Map of String prefixes (keys) and the values associated with them
   * @param values The Map which contains the keys and the values
   * @tparam K The type of the keys
   * @tparam V The type of the values
   * @return The TrieMap containing the String prefixes and values
   */
  def build[K, V](values: Map[Seq[K], V]): TrieMap[K, V] = {
    values.foldLeft(empty[K, V]) { case (map, (key, value)) =>
      insert(map, key, value)
    }
  }

  private def toMap[K, V](map: TrieMap[K, V]): Map[Seq[K], V] = {
    def toMap(map: TrieMap[K, V], prefix: Seq[K]): Map[Seq[K], V] = {
        map.value.map(v => prefix -> v).toMap ++
          map.children.flatMap { case (k, trie) =>
          toMap(trie, prefix :+ k)
        }
      }
    toMap(map, Seq.empty[K])
  }

  private def insert[K, V](map: TrieMap[K, V], key: Seq[K], value: V): TrieMap[K, V] = {
    if (key.isEmpty) {
      require(map.value.isEmpty, s"error, value already present: ${map.value}")
      map.copy(value = Some(value))
    } else {
      if (map.children.contains(key.head)) {
        map.copy(children = map.children.updated(key.head, insert(map.children(key.head), key.tail, value)))
      } else {
        map.copy(children = map.children + (key.head -> insert(TrieMap.empty, key.tail, value)))
      }
    }
  }

  private def dump[K, V](map: TrieMap[K, V], depth: Int = 0): String = {
    val tabs = "\t" * depth
    map.value.getOrElse("").toString + ": " + map.children.map{ case (k, m) => s"\n$tabs$k -> ${dump(m, depth + 1)}"}.mkString
  }

  @tailrec
  private def findPrefixFor[K, V](map: TrieMap[K, V], key: Seq[K]): Option[V] = {
    if(key.isEmpty || map.children.isEmpty) {
      map.value
    } else {
      if (map.children.contains(key.head)) {
        findPrefixFor(map.children(key.head), key.tail)
      } else {
        map.value
      }
    }
  }
}
