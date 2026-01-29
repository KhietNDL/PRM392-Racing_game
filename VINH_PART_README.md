# Vinh's Part - Sound & Result Screen

## 📋 Overview
This part implements the sound system and result screens for the Racing Game project.

## 🎯 Features Implemented

### 1. Sound System (`MusicManager.java`)
- **Background Music (BGM)**: Uses `MediaPlayer` for looped background music
- **Sound Effects**: Uses `SoundPool` for short sound effects (low latency)
- **Lifecycle Management**: Proper pause/resume/stop handling
- **Singleton Pattern**: One instance throughout the app

#### Key Methods:
```java
MusicManager.getInstance().startBgm(context, R.raw.congratulation_bgm);
MusicManager.getInstance().stopBgm();
MusicManager.getInstance().pauseBgm();
MusicManager.getInstance().resumeBgm();
MusicManager.getInstance().playEffect(context, R.raw.win_sound);
```

### 2. Winner Screen (`WinnerActivity.java`)
- Shows winner icon (trophy)
- Displays winner horse/car image
- Shows winner name/number
- Plays congratulation BGM
- Entrance animations (scale + fade)
- **Next button** → goes to Bet Result Screen
- **Back button disabled** (user must click Next)

#### Intent Extras Required:
```java
intent.putExtra("winnerId", 1);
intent.putExtra("winnerName", "Horse 1");
intent.putExtra("winnerImageRes", R.drawable.horse1);
intent.putExtra("moneyBefore", 1000);
intent.putExtra("moneyAfter", 1500);
intent.putExtra("betAmount", 200);
intent.putExtra("betOnId", 1);
```

### 3. Bet Result Screen (`BetResultActivity.java`)
- Shows win/loss icon and message
- Displays money before race (white color)
- Displays money change (+/- in green/red)
- Displays money after race (green if higher, red if lower)
- **Play Again button** → return to betting/game screen
- **Home button** → return to home page
- Stops congratulation BGM when leaving
- **Back button disabled** (user must use Home or Play Again)

#### Money Display Logic:
- **Money Before**: White color - shows previous balance
- **Money Change**: 
  - Green (`+500`) if profit
  - Red (`-200`) if loss
- **Money After**: 
  - Green if higher than before
  - Red if lower than before
  - White if same

### 4. Flow Diagram
```
Racing Game
    |
    | (race finished)
    ↓
WinnerActivity (plays congratulation BGM)
    |
    | click [Next] button (can't go back)
    ↓
BetResultActivity (BGM continues)
    |
    ├─→ click [Home] → MainActivity (stop BGM)
    └─→ click [Play Again] → MainActivity (stop BGM)
```

## 📁 Files Created/Modified

### Java Files:
1. **`sound/MusicManager.java`** - Enhanced sound manager
2. **`ui/WinnerActivity.java`** - Winner screen logic
3. **`ui/BetResultActivity.java`** - Bet result screen logic

### Layout Files:
1. **`layout/activity_winner.xml`** - Winner screen UI
2. **`layout/activity_bet_result.xml`** - Bet result screen UI

### Drawable Resources:
1. **`drawable/ic_trophy.xml`** - Trophy icon (gold)
2. **`drawable/ic_win.xml`** - Win icon (green checkmark)
3. **`drawable/ic_lose.xml`** - Lose icon (red exclamation)

### Resource Files:
1. **`values/colors.xml`** - Added win_green, lose_red, bg_dark, gold colors

### Audio Files Needed (in `res/raw/`):
1. **`congratulation_bgm.mp3`** - Background music for winner & result screens
2. **`win_sound.mp3`** - Victory sound effect
3. **`race_sound.mp3`** - Racing background music
4. **`login_sound.mp3`** - Login screen music (already exists)

## 🔧 Integration Guide

### For GameActivity Developer (Đức Anh):
When race finishes, call WinnerActivity:

```java
// In GameActivity when race ends
int winnerId = 1; // The winner horse/car ID
String winnerName = "Horse 1";
int winnerImageRes = R.drawable.horse1; // or R.drawable.car1

Intent intent = new Intent(GameActivity.this, WinnerActivity.class);
intent.putExtra("winnerId", winnerId);
intent.putExtra("winnerName", winnerName);
intent.putExtra("winnerImageRes", winnerImageRes);
intent.putExtra("moneyBefore", playerMoneyBefore);
intent.putExtra("moneyAfter", playerMoneyAfter);
intent.putExtra("betAmount", betAmount);
intent.putExtra("betOnId", playerBetId);
startActivity(intent);
finish();
```

### For Bet Manager Developer (Tín):
Provide money calculation:
```java
// Before race
int moneyBefore = Player.getMoney();
int betAmount = Player.getBetAmount();
int betOnId = Player.getBetOnId();

// After race
int moneyAfter = BetManager.calculateMoney(moneyBefore, betAmount, isWin);
```

## 🎨 UI Elements

### Winner Screen:
- Trophy icon at top
- Winner title with emojis
- Large winner image (horse/car)
- Celebration text
- Gold "Next" button

### Bet Result Screen:
- Win/Lose icon
- Result title (green for win, red for loss)
- Money card showing:
  - Previous money (white)
  - Change amount (green/red)
  - Current money (green/red)
- Play Again button (green)
- Home button (gold)

## 🎵 Sound Requirements

### Audio Files to Add:
Place these MP3 files in `app/src/main/res/raw/`:

1. **`congratulation_bgm.mp3`** 
   - Looped background music
   - Plays from WinnerActivity → BetResultActivity
   - Stops when user clicks Home or Play Again

2. **`win_sound.mp3`** (optional)
   - Short victory fanfare
   - Plays once when showing winner

3. **`race_sound.mp3`**
   - Racing background music
   - Used by GameActivity

## 🧪 Testing Checklist

- [ ] BGM plays when WinnerActivity starts
- [ ] BGM continues when moving to BetResultActivity
- [ ] BGM stops when clicking Home or Play Again
- [ ] Winner image displays correctly
- [ ] Money colors display correctly (white/green/red)
- [ ] Animations play smoothly
- [ ] Back button is disabled on both screens
- [ ] Next button works (can't return to WinnerActivity)
- [ ] Home button returns to MainActivity
- [ ] Play Again button returns to game/betting screen
- [ ] No memory leaks (MediaPlayer properly released)

## 🔑 Key Techniques Used

1. **MediaPlayer** - For background music (looped)
2. **SoundPool** - For sound effects (low latency)
3. **Singleton Pattern** - MusicManager instance
4. **Intent Extras** - Passing data between activities
5. **View Animations** - ScaleAnimation, AlphaAnimation
6. **Lifecycle Callbacks** - onPause, onResume, onDestroy
7. **ContextCompat** - Getting colors safely
8. **Constraint Layout** - Responsive UI design

## 📞 Integration Points

### Activities that call your screens:
- **GameActivity** → WinnerActivity (when race ends)
- **WinnerActivity** → BetResultActivity (when user clicks Next)
- **BetResultActivity** → MainActivity (when user clicks Home/Play Again)

### Your screens update:
- Player money (from BetManager/Player classes)
- Winner information (from GameActivity)

## 💡 Notes

- Both WinnerActivity and BetResultActivity disable back button
- BGM plays continuously from Winner → BetResult screen
- User MUST click Next/Home/Play Again to navigate
- Money display uses 3 states: before (white), change (+/- green/red), after (green/red/white)
- All screens use dark background (#1E1E1E) for consistency

---

**Developer**: Vinh  
**Part**: Sound & Result Screen  
**Status**: ✅ Complete
