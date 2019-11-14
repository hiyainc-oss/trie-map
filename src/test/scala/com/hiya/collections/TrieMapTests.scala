package com.hiya.collections

import org.scalatest.{FunSpec, Matchers}

import scala.collection.immutable.HashMap

class TrieMapTests extends FunSpec with Matchers {
  describe("TrieMap") {
    describe("empty") {
      it("should return empty TrieMap") {
        val trieMap = TrieMap.empty[Char, Int]
        trieMap.value shouldBe None
        trieMap.children shouldBe HashMap.empty[Char, TrieMap[Char, Int]]
      }
    }
    describe("insert && find") {
      it("should insert a value to an empty TrieMap") {
        TrieMap.empty[Char, Int]
          .insert("pre", 5)
          .findPrefixFor("prefix") shouldBe Some(5)
      }
      it("should insert values with common prefixes && should find longest prefix") {
        val map = TrieMap.empty[Char, Int]
          .insert("pre", 2)
          .insert("pr", 1)
        map.findPrefixFor("prefix") shouldBe Some(2)
        map.findPrefixFor("professor") shouldBe Some(1)
      }
      it("should insert & find values with no common prefixes"){
        val map = TrieMap.empty[Char, Int]
          .insert("pre", 1)
          .insert("anotherpre", 2)
        map.findPrefixFor("prefix")  shouldBe Some(1)
        map.findPrefixFor("anotherprefix") shouldBe Some(2)
      }
      it("should not find string key with search in empty TrieMap") {
        val map = TrieMap.empty[Char, Int]
        map.findPrefixFor("prefix") shouldBe None
      }
      it("should not overwrite value already present"){
        val map = TrieMap.empty[Char, Int]
          .insert("pre", 1)
        an[IllegalArgumentException] shouldBe thrownBy(map.insert("pre", 2))
      }
    }
    describe("contains"){
      it("should return true for key when it is found in TrieMap"){
        val map = TrieMap.empty[Char, Int]
          .insert("pre",1)
        map.containsPrefixFor("prefix") shouldBe true
      }
      it("should return false for key when it is not found in TrieMap"){
        val map = TrieMap.empty[Char, Int]
          .insert("pro",1)
        map.containsPrefixFor("prefix") shouldBe false
      }
    }
    describe("build"){
      it("should build empty TrieMap from empty prefix map") {
        val prefixes = Map[Seq[Char], Int]()
        TrieMap.build(prefixes) shouldBe TrieMap.empty[Char, Int]
      }
      it("should build TrieMap from prefix map") {
        val prefixes = Map[Seq[Char], Int]("pre".toSeq -> 1, "another".toSeq -> 2)
        TrieMap.build(prefixes) shouldBe TrieMap.empty[Char, Int].insert("pre", 1).insert("another", 2)
      }
      it("should build TrieMap from prefix map with overlapping prefixes") {
        val prefixes = Map[Seq[Char], Int]("pre".toSeq -> 1, "pref".toSeq -> 2)
        TrieMap.build(prefixes) shouldBe TrieMap.empty[Char, Int].insert("pre", 1).insert("pref", 2)
      }
    }
    describe("toMap"){
      it("should convert TrieMap to ordinary Map"){
        val prefixes = Map[Seq[Char], Int]("pre".toSeq -> 1, "pref".toSeq -> 2)
        TrieMap.build(prefixes).toMap shouldBe prefixes
      }
    }
  }
}
