package com.example.numberadditionsubtractionandroid.data

data class Animal(
    val emoji: String,
    val nameChinese: String,
    val nameJapanese: String,
    val nameEnglish: String
)

fun Animal.localizedName(language: AppLanguage) = when (language) {
    AppLanguage.CHINESE -> nameChinese
    AppLanguage.ENGLISH -> nameEnglish
    AppLanguage.JAPANESE -> nameJapanese
}

val ALL_ANIMALS = listOf(
    Animal("\uD83D\uDC36", "狗", "いぬ", "Dog"),
    Animal("\uD83D\uDC31", "貓", "ねこ", "Cat"),
    Animal("\uD83D\uDC30", "兔子", "うさぎ", "Rabbit"),
    Animal("\uD83D\uDC3B", "熊", "くま", "Bear"),
    Animal("\uD83D\uDC3C", "熊貓", "パンダ", "Panda"),
    Animal("\uD83D\uDC37", "豬", "ぶた", "Pig"),
    Animal("\uD83D\uDC2E", "牛", "うし", "Cow"),
    Animal("\uD83D\uDC38", "青蛙", "かえる", "Frog"),
    Animal("\uD83D\uDC35", "猴子", "さる", "Monkey"),
    Animal("\uD83E\uDD81", "獅子", "ライオン", "Lion"),
    Animal("\uD83D\uDC2F", "老虎", "とら", "Tiger"),
    Animal("\uD83D\uDC14", "雞", "にわとり", "Chicken"),
    Animal("\uD83D\uDC27", "企鵝", "ペンギン", "Penguin"),
    Animal("\uD83D\uDC33", "鯨魚", "くじら", "Whale"),
    Animal("\uD83D\uDC18", "大象", "ぞう", "Elephant"),
    Animal("\uD83E\uDD92", "長頸鹿", "キリン", "Giraffe"),
    Animal("\uD83D\uDC11", "羊", "ひつじ", "Sheep"),
    Animal("\uD83D\uDC34", "馬", "うま", "Horse"),
    Animal("\uD83E\uDD8B", "蝴蝶", "ちょうちょ", "Butterfly"),
    Animal("\uD83D\uDC22", "烏龜", "かめ", "Turtle")
)

