package com.example.ahmadnemati.mfa

class PersonKt {

    lateinit var name: String
    var lifeWishes: Int = 0
    var deathWishes: Int = 0
    var charcterK: CharacterK = CharacterK.Citizen

    enum class CharacterK {
        Mafia,
        Citizen,
        Doctor,
        Achilles,
        Dynamite
    }

    constructor(name: String, lifeWishes: Int, deathWishes: Int, characterK: CharacterK) {
        this.name = name
        this.lifeWishes = lifeWishes
        this.deathWishes = deathWishes
        this.charcterK = characterK
    }

    constructor(name: String) {
        PersonKt(name, 0, 0, CharacterK.Citizen)
    }

}
