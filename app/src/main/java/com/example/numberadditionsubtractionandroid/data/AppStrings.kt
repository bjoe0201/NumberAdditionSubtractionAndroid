package com.example.numberadditionsubtractionandroid.data

enum class AppLanguage { CHINESE, ENGLISH, JAPANESE }

object AppStrings {
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "數字加減"
        AppLanguage.ENGLISH -> "Number Math"
        AppLanguage.JAPANESE -> "かずのたしひき"
    }

    fun start(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "開始！"
        AppLanguage.ENGLISH -> "Start!"
        AppLanguage.JAPANESE -> "はじめよう！"
    }

    fun settings(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "設定"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.JAPANESE -> "せってい"
    }

    fun leaderboard(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "排行榜"
        AppLanguage.ENGLISH -> "Leaderboard"
        AppLanguage.JAPANESE -> "ランキング"
    }

    fun correct(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答對了！"
        AppLanguage.ENGLISH -> "Correct!"
        AppLanguage.JAPANESE -> "せいかい！"
    }

    fun wrong(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答錯了！"
        AppLanguage.ENGLISH -> "Wrong!"
        AppLanguage.JAPANESE -> "ざんねん！"
    }

    fun roundProgress(lang: AppLanguage, current: Int, total: Int) = when (lang) {
        AppLanguage.CHINESE -> "第$current/$total 題"
        AppLanguage.ENGLISH -> "Round $current/$total"
        AppLanguage.JAPANESE -> "第 $current/$total 問"
    }

    fun howMany(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "有幾隻？"
        AppLanguage.ENGLISH -> "How many?"
        AppLanguage.JAPANESE -> "何匹いる？"
    }

    fun rounds(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "題數"
        AppLanguage.ENGLISH -> "Rounds"
        AppLanguage.JAPANESE -> "もんだい数"
    }

    fun maxCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "最大數量"
        AppLanguage.ENGLISH -> "Max Count"
        AppLanguage.JAPANESE -> "さいだいかず"
    }

    fun voice(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "語音"
        AppLanguage.ENGLISH -> "Voice"
        AppLanguage.JAPANESE -> "こえ"
    }

    fun voiceNone(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "關閉"
        AppLanguage.ENGLISH -> "None"
        AppLanguage.JAPANESE -> "なし"
    }

    fun voiceNumber(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "數字"
        AppLanguage.ENGLISH -> "Number"
        AppLanguage.JAPANESE -> "数字"
    }

    fun voiceNumberWithAnimal(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "數字+動物"
        AppLanguage.ENGLISH -> "Number + Animal"
        AppLanguage.JAPANESE -> "数字+どうぶつ"
    }

    fun language(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "語言"
        AppLanguage.ENGLISH -> "Language"
        AppLanguage.JAPANESE -> "言語"
    }

    fun back(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "返回"
        AppLanguage.ENGLISH -> "Back"
        AppLanguage.JAPANESE -> "もどる"
    }

    fun score(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "分數"
        AppLanguage.ENGLISH -> "Score"
        AppLanguage.JAPANESE -> "スコア"
    }

    fun correctCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答對"
        AppLanguage.ENGLISH -> "Correct"
        AppLanguage.JAPANESE -> "せいかい"
    }

    fun wrongCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答錯"
        AppLanguage.ENGLISH -> "Wrong"
        AppLanguage.JAPANESE -> "まちがい"
    }

    fun yourName(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "你的名字"
        AppLanguage.ENGLISH -> "Your Name"
        AppLanguage.JAPANESE -> "なまえ"
    }

    fun saveScore(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "儲存分數"
        AppLanguage.ENGLISH -> "Save Score"
        AppLanguage.JAPANESE -> "スコアを保存"
    }

    fun playAgain(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "再玩一次"
        AppLanguage.ENGLISH -> "Play Again"
        AppLanguage.JAPANESE -> "もういちど"
    }

    fun home(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "回首頁"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.JAPANESE -> "ホーム"
    }

    fun selectLanguage(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "選擇語言"
        AppLanguage.ENGLISH -> "Select Language"
        AppLanguage.JAPANESE -> "言語をえらぼう"
    }

    fun on(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "開"
        AppLanguage.ENGLISH -> "On"
        AppLanguage.JAPANESE -> "オン"
    }

    fun off(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "關"
        AppLanguage.ENGLISH -> "Off"
        AppLanguage.JAPANESE -> "オフ"
    }

    fun tapToCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點點動物來數數！"
        AppLanguage.ENGLISH -> "Tap to count!"
        AppLanguage.JAPANESE -> "タップして数えよう！"
    }

    fun date(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "日期"
        AppLanguage.ENGLISH -> "Date"
        AppLanguage.JAPANESE -> "日付"
    }

    fun clearLeaderboard(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "清除排行榜"
        AppLanguage.ENGLISH -> "Clear Leaderboard"
        AppLanguage.JAPANESE -> "ランキングをクリア"
    }

    fun clearConfirm1(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "確定清除所有紀錄？"
        AppLanguage.ENGLISH -> "Clear all records?"
        AppLanguage.JAPANESE -> "すべての記録を消しますか？"
    }

    fun clearConfirm2(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "真的確定嗎？無法復原！"
        AppLanguage.ENGLISH -> "Really sure? Cannot be undone!"
        AppLanguage.JAPANESE -> "本当にいいですか？元に戻せません！"
    }

    fun clearConfirmBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "確定清除"
        AppLanguage.ENGLISH -> "Yes, Clear"
        AppLanguage.JAPANESE -> "クリアする"
    }

    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "取消"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.JAPANESE -> "キャンセル"
    }

    fun clearDone(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "已清除完畢！"
        AppLanguage.ENGLISH -> "Cleared!"
        AppLanguage.JAPANESE -> "クリアしました！"
    }

    fun changeLanguage(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "換語言"
        AppLanguage.ENGLISH -> "Change Language"
        AppLanguage.JAPANESE -> "言語をかえる"
    }

    fun addition(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "加法"
        AppLanguage.ENGLISH -> "Addition"
        AppLanguage.JAPANESE -> "たしざん"
    }

    fun subtraction(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "減法"
        AppLanguage.ENGLISH -> "Subtraction"
        AppLanguage.JAPANESE -> "ひきざん"
    }

    fun mixed(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "混合"
        AppLanguage.ENGLISH -> "Mixed"
        AppLanguage.JAPANESE -> "ミックス"
    }

    fun gameMode(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "遊戲模式"
        AppLanguage.ENGLISH -> "Game Mode"
        AppLanguage.JAPANESE -> "ゲームモード"
    }

    fun tapToAdd(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點動物來加加看！"
        AppLanguage.ENGLISH -> "Tap to add!"
        AppLanguage.JAPANESE -> "タップしてたそう！"
    }

    fun tapToCross(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點動物畫X來減！"
        AppLanguage.ENGLISH -> "Tap to cross out!"
        AppLanguage.JAPANESE -> "タップしてけそう！"
    }
}

