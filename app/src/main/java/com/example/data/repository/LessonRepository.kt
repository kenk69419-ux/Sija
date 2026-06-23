package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.database.AppDatabase
import com.example.data.model.Lesson
import com.example.data.model.Question
import com.example.data.model.UserProgress
import com.example.data.model.XpLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LessonRepository(private val db: AppDatabase) {

    val userProgress: Flow<UserProgress?> = db.userProgressDao().getUserProgressFlow()
    val allLessons: Flow<List<Lesson>> = db.lessonDao().getAllLessonsFlow()
    val xpLogs: Flow<List<XpLog>> = db.xpLogDao().getAllXpLogsFlow()

    fun getLessonsByUnit(unitId: Int): Flow<List<Lesson>> = db.lessonDao().getLessonsByUnitFlow(unitId)

    suspend fun getQuestionsForLesson(lessonId: Int): List<Question> = withContext(Dispatchers.IO) {
        db.questionDao().getQuestionsForLesson(lessonId)
    }

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        val userProgressDao = db.userProgressDao()
        val lessonDao = db.lessonDao()
        val questionDao = db.questionDao()

        // 1. Seed default user progress if not exists
        var progress = userProgressDao.getUserProgress()
        if (progress == null) {
            progress = UserProgress(
                id = 1,
                name = "Sobat Sinau",
                xp = 0,
                streak = 0,
                lastActiveTime = 0L,
                levelIndex = 1,
                hearts = 5
            )
            userProgressDao.insertUserProgress(progress)
            Log.d("LessonRepository", "User progress seeded")
        }

        // 2. Seed Javanese lessons
        val initialLessons = listOf(
            // Unit 1: Ngoko (Informal/Daily Javanese) - Unit ID: 1
            Lesson(1, 1, "Sapa Jenengmu?", "Perkenalan sehari-hari dengan teman sebaya.", "Ngoko", false, 5),
            Lesson(2, 1, "Rega Pira?", "Kosakata angka & tawar-menawar di pasar.", "Ngoko", false, 5),
            Lesson(3, 1, "Dolan Bareng", "Aktivitas sehari-hari & bermain bersama kanca.", "Ngoko", false, 5),

            // Unit 2: Krama Madya (Polite Javanese) - Unit ID: 2
            Lesson(4, 2, "Asma Sampeyan Sapa?", "Perkenalan sopan dengan orang baru.", "Krama", false, 5),
            Lesson(5, 2, "Pinten Arganipun?", "Belanja sopan & bertransaksi jual-beli.", "Krama", false, 5),
            Lesson(6, 2, "Kesah dhateng Peken", "Mengobrol sopan tentang kegiatan harian.", "Krama", false, 5),

            // Unit 3: Krama Inggil (Highest Respect Javanese) - Unit ID: 3
            Lesson(7, 3, "Sugeng Rawuh", "Menyambut & memberikan hormat pada orang tua.", "Krama Inggil", false, 5),
            Lesson(8, 3, "Mustaka & Pasuryan", "Belajar kosakata bagian tubuh tingkat halus.", "Krama Inggil", false, 5),
            Lesson(9, 3, "Suraos Uleman Resmi", "Bahasa resmi untuk upacara & undangan adat.", "Krama Inggil", false, 5)
        )
        lessonDao.insertLessons(initialLessons)

        // 3. Seed curriculum questions (5 questions per lesson = 45 Javanese quiz cards)
        val questions = mutableListOf<Question>()

        // --- LESSON 1 QUESTIONS (Ngoko Intro) ---
        questions.add(Question(
            lessonId = 1, type = "PILIHAN_GANDA",
            prompt = "Jenengku Budi. Kowe sapa?",
            secondaryPrompt = "Artikan kalimat perkenalan Ngoko di atas!",
            correctAnswer = "Namaku Budi. Kamu siapa?",
            wrongOptionsCsv = "Namaku Budi. Dia siapa?,Nama saya Budi. Anda siapa?,Nama kamu Budi. Saya siapa?",
            phoneticText = "Je-neng-ku Bu-di. Ko-we sa-pa?"
        ))
        questions.add(Question(
            lessonId = 1, type = "SUSUN_KATA",
            prompt = "Kabarmu piye, kanca?",
            secondaryPrompt = "Susunlah kalimat Javanese terjemahan dari: 'Bagaimana kabarmu, teman?'",
            correctAnswer = "Kabarmu piye kanca",
            wrongOptionsCsv = "kanca,piye,Kabarmu,sampeyan,pripun",
            phoneticText = "Ka-bar-mu pi-ye kan-ca?"
        ))
        questions.add(Question(
            lessonId = 1, type = "LISTENING",
            prompt = "Aku arep lunga dhisik ya.",
            secondaryPrompt = "Dengarkan/baca logat ini. Apa arti kata 'lunga'?",
            correctAnswer = "pergi",
            wrongOptionsCsv = "makan,tidur,pulang",
            phoneticText = "A-ku a-rep lung-a dhi-sik ya"
        ))
        questions.add(Question(
            lessonId = 1, type = "PILIHAN_GANDA",
            prompt = "Kowe saiki manggon ing ngendi?",
            secondaryPrompt = "Tentukan arti dari pertanyaan Ngoko di atas!",
            correctAnswer = "Kamu sekarang tinggal di mana?",
            wrongOptionsCsv = "Kamu kemarin pulang dari mana?,Kamu mau tidur di mana?,Anda sekarang kerja di mana?",
            phoneticText = "Ko-we sa-i-ki mang-gon ing ngen-di?"
        ))
        questions.add(Question(
            lessonId = 1, type = "SUSUN_KATA",
            prompt = "Aku arep mangan sega",
            secondaryPrompt = "Susun kalimat Javanese dari: 'Saya mau makan nasi'",
            correctAnswer = "Aku arep mangan sega",
            wrongOptionsCsv = "Sega,arep,mangan,Aku,nedha,kula",
            phoneticText = "A-ku a-rep ma-ngan se-ga"
        ))

        // --- LESSON 2 QUESTIONS (Ngoko Numbers & Shopping) ---
        questions.add(Question(
            lessonId = 2, type = "PILIHAN_GANDA",
            prompt = "Rega buku iki pira?",
            secondaryPrompt = "Artikan kalimat tawar-menawar ini!",
            correctAnswer = "Harga buku ini berapa?",
            wrongOptionsCsv = "Beli buku ini berapa?,Membawa buku ini ke mana?,Harga tas ini berapa?",
            phoneticText = "Re-ga bu-ku i-ki pi-ra?"
        ))
        questions.add(Question(
            lessonId = 2, type = "PILIHAN_GANDA",
            prompt = "Siji, Loro, Telu, Papat, Lima. Apa arti 'Loro'?",
            secondaryPrompt = "Tentukan angka yang tepat dalam bahasa Ngoko!",
            correctAnswer = "Dua",
            wrongOptionsCsv = "Satu,Tiga,Empat",
            phoneticText = "Si-ji, Lo-ro, Te-lu, Pa-pat, Li-ma"
        ))
        questions.add(Question(
            lessonId = 2, type = "SUSUN_KATA",
            prompt = "Rega lombok saiki larang banget",
            secondaryPrompt = "Susun terjemahan dari: 'Harga cabai sekarang mahal sekali'",
            correctAnswer = "Rega lombok saiki larang banget",
            wrongOptionsCsv = "saiki,larang,Rega,lombok,banget,mirah,awis",
            phoneticText = "Re-ga lom-bok sa-i-ki la-rang ba-nget"
        ))
        questions.add(Question(
            lessonId = 2, type = "LISTENING",
            prompt = "Dhuwite keri ing omah.",
            secondaryPrompt = "Pahami logat Ngoko. Apa arti kata 'dhuwit'?",
            correctAnswer = "uang",
            wrongOptionsCsv = "dompet,tas,buku",
            phoneticText = "Dhu-wi-te ke-ri ing o-mah"
        ))
        questions.add(Question(
            lessonId = 2, type = "PILIHAN_GANDA",
            prompt = "Iki murah, regane mung limang ewu.",
            secondaryPrompt = "Artikan kalimat di atas!",
            correctAnswer = "Ini murah, harganya cuma lima ribu.",
            wrongOptionsCsv = "Ini mahal, harganya sampai sepuluh ribu.,Ini murah, harganya cuma tiga ribu.,Buku ini bagus, harganya lima ribu.",
            phoneticText = "I-ki mu-rah, re-ga-ne mung li-mang e-wu"
        ))

        // --- LESSON 3 QUESTIONS (Ngoko Daily Activities) ---
        questions.add(Question(
            lessonId = 3, type = "PILIHAN_GANDA",
            prompt = "Aja turu sore-sore, ayo dolanan dhisik!",
            secondaryPrompt = "Artikan himbauan Ngoko berikut!",
            correctAnswer = "Jangan tidur sore-sore, ayo bermain dulu!",
            wrongOptionsCsv = "Jangan tidur malam-malam, ayo makan dulu!,Ayo pulang bersama teman-teman!,Ayo bangun tidur dan mandi dulu!",
            phoneticText = "A-ja tu-ru so-re so-re, a-yo do-la-nan dhi-sik!"
        ))
        questions.add(Question(
            lessonId = 3, type = "LISTENING",
            prompt = "Budi wis mangan karo iwak pitik.",
            secondaryPrompt = "Kosakata makan sehari-hari. Apa arti kata 'mangan' dan 'iwak pitik'?",
            correctAnswer = "Makan dan lauk ayam",
            wrongOptionsCsv = "Minum dan lauk telur,Makan dan lauk ikan laut,Tidur dan lauk kambing",
            phoneticText = "Bu-di wis ma-ngan ka-ro i-wak pi-tik"
        ))
        questions.add(Question(
            lessonId = 3, type = "SUSUN_KATA",
            prompt = "Aku arep nderek kowe lunga",
            secondaryPrompt = "Susun terjemahan: 'Saya ingin ikut kamu pergi'",
            correctAnswer = "Aku arep nderek kowe lunga",
            wrongOptionsCsv = "nderek,Aku,kowe,lunga,arep,kula,sampeyan",
            phoneticText = "A-ku a-rep nde-rek ko-we lung-a"
        ))
        questions.add(Question(
            lessonId = 3, type = "PILIHAN_GANDA",
            prompt = "Dolan bareng kanca pancen seneng.",
            secondaryPrompt = "Pilih arti kata utama 'kanca' di kalimat di atas!",
            correctAnswer = "teman",
            wrongOptionsCsv = "keluarga,tetangga,guru",
            phoneticText = "Do-lan ba-reng kan-ca pan-cen se-neng"
        ))
        questions.add(Question(
            lessonId = 3, type = "PILIHAN_GANDA",
            prompt = "Aku arep adus dhisik.",
            secondaryPrompt = "Apa arti kata kerja 'adus'?",
            correctAnswer = "mandi",
            wrongOptionsCsv = "makan,berlari,menyanyi",
            phoneticText = "A-ku a-rep a-dus dhi-sik"
        ))

        // --- LESSON 4 QUESTIONS (Krama Intro) ---
        questions.add(Question(
            lessonId = 4, type = "PILIHAN_GANDA",
            prompt = "Nami kula Ahmad. Asma sampeyan sapa?",
            secondaryPrompt = "Artikan kalimat perkenalan sopan (Krama Madya) di atas!",
            correctAnswer = "Nama saya Ahmad. Nama Anda siapa?",
            wrongOptionsCsv = "Nama saya Ahmad. Nama dia siapa?,Nama kamu Ahmad. Saya siapa?,Namaku Ahmad. Kamu siapa?",
            phoneticText = "Na-mi ku-la Ah-mad. As-ma sam-pe-yan sa-pa?"
        ))
        questions.add(Question(
            lessonId = 4, type = "SUSUN_KATA",
            prompt = "Pripun kabaripun sampeyan",
            secondaryPrompt = "Susunlah kalimat terjemahan sopan dari: 'Bagaimana kabar Anda?'",
            correctAnswer = "Pripun kabaripun sampeyan",
            wrongOptionsCsv = "sampeyan,kabaripun,Pripun,piye,kabarmu,kowe",
            phoneticText = "Pri-pun ka-ba-ri-pun sam-pe-yan?"
        ))
        questions.add(Question(
            lessonId = 4, type = "LISTENING",
            prompt = "Kula badhe kesah rumiyin.",
            secondaryPrompt = "Bandingkan kata 'lunga' (Ngoko) dan 'kesah' (Krama). Keduanya berarti...",
            correctAnswer = "Pergi",
            wrongOptionsCsv = "Makan,Tidur,Kembali",
            phoneticText = "Ku-la ba-dhe ke-sah ru-mi-yin"
        ))
        questions.add(Question(
            lessonId = 4, type = "PILIHAN_GANDA",
            prompt = "Kula manggen wonten ing Sala.",
            secondaryPrompt = "Pilih arti kata 'manggen wonten ing' dalam Bahasa Indonesia!",
            correctAnswer = "tinggal berada di",
            wrongOptionsCsv = "bekerja di dekat,lahir di sekitar,jalan-jalan di",
            phoneticText = "Ku-la mang-gen won-ten ing Sa-la"
        ))
        questions.add(Question(
            lessonId = 4, type = "PILIHAN_GANDA",
            prompt = "Menawi sampeyan saking pundi?",
            secondaryPrompt = "Artikan pertanyaan Krama berikut ini!",
            correctAnswer = "Kalau Anda berasal dari mana?",
            wrongOptionsCsv = "Kalau dia mau menginap di mana?,Apakah Anda sudah makan di mana?,Kamu mau bermain ke mana?",
            phoneticText = "Me-na-wi sam-pe-yan sa-king pun-di?"
        ))

        // --- LESSON 5 QUESTIONS (Krama Numbers & Shopping) ---
        questions.add(Question(
            lessonId = 5, type = "PILIHAN_GANDA",
            prompt = "Pinten arganipun buku menika?",
            secondaryPrompt = "Artikan kalimat belanja sopan ini!",
            correctAnswer = "Berapa harganya buku ini?",
            wrongOptionsCsv = "Di mana diletakkan buku ini?,Siapa yang menulis buku ini?,Kenapa mahal sekali buku ini?",
            phoneticText = "Pin-ten ar-ga-ni-pun bu-ku me-ni-ka?"
        ))
        questions.add(Question(
            lessonId = 5, type = "PILIHAN_GANDA",
            prompt = "Setunggal, Kalih, Tiga, Sekawan, Gansal. Gansal artinya...",
            secondaryPrompt = "Ingat angka Krama Madya!",
            correctAnswer = "Lima",
            wrongOptionsCsv = "Empat,Tiga,Dua",
            phoneticText = "Se-tung-gal, Ka-lih, Ti-ga, Se-ka-wan, Gan-sal"
        ))
        questions.add(Question(
            lessonId = 5, type = "SUSUN_KATA",
            prompt = "Arta kula kantun sekedhik",
            secondaryPrompt = "Susun kalimat Krama: 'Uang saya tinggal sedikit'",
            correctAnswer = "Arta kula kantun sekedhik",
            wrongOptionsCsv = "kantun,Arta,kula,sekedhik,dhuwit,aku,sekedhik",
            phoneticText = "Ar-ta ku-la kan-tun se-ke-dhik"
        ))
        questions.add(Question(
            lessonId = 5, type = "LISTENING",
            prompt = "Arganipun abrit punika awis sanget.",
            secondaryPrompt = "Pahami kosa kata ekonomi tingkat Krama. 'Awis' artinya...",
            correctAnswer = "Mahal",
            wrongOptionsCsv = "Murah,Sedang,Kurang",
            phoneticText = "Ar-ga-ni-pun ab-rit pu-ni-ka a-wis sa-nget"
        ))
        questions.add(Question(
            lessonId = 5, type = "PILIHAN_GANDA",
            prompt = "Menika mirah, arganipun namung tigang ewu.",
            secondaryPrompt = "Artikan kalimat di atas!",
            correctAnswer = "Ini murah, harganya cuma tiga ribu.",
            wrongOptionsCsv = "Ini mahal, harganya cuma delapan ribu.,Ini bagus, harganya cuma lima ribu.,Ini murah, harganya cuma sepuluh ribu.",
            phoneticText = "Me-ni-ka mi-rah, ar-ga-ni-pun na-mung ti-gang e-wu"
        ))

        // --- LESSON 6 QUESTIONS (Krama Activities) ---
        questions.add(Question(
            lessonId = 6, type = "PILIHAN_GANDA",
            prompt = "Sampun tilem rumiyin, monggo nedha sesarengan.",
            secondaryPrompt = "Artikan kalimat harian Krama ini!",
            correctAnswer = "Jangan tidur dulu, mari makan bersama-sama.",
            wrongOptionsCsv = "Sudah pulang dulu, ayo berjalan bersama.,Silakan duduk dhisik, ayo minum teh.,Jangan marah dulu, ayo bernyanyi riang.",
            phoneticText = "Sam-pun ti-lem ru-mi-yin, mong-go ne-dha se-sa-re-ngan"
        ))
        questions.add(Question(
            lessonId = 6, type = "LISTENING",
            prompt = "Kula kaliyan kanca badhe tumbas sega goreng.",
            secondaryPrompt = "Kata 'tumbas' (Krama) sepadan dengan 'tuku' (Ngoko), yang berarti...",
            correctAnswer = "membeli",
            wrongOptionsCsv = "menjual,memasak,memakan",
            phoneticText = "Ku-la ka-li-yan kan-ca ba-dhe tum-bas se-ga go-reng"
        ))
        questions.add(Question(
            lessonId = 6, type = "SUSUN_KATA",
            prompt = "Kula badhe kesah dhateng peken",
            secondaryPrompt = "Susun kalimat sopan: 'Saya mau pergi ke pasar'",
            correctAnswer = "Kula badhe kesah dhateng peken",
            wrongOptionsCsv = "kesah,Kula,badhe,dhateng,peken,lunga,pasar",
            phoneticText = "Ku-la ba-dhe ke-sah dha-teng pe-ken"
        ))
        questions.add(Question(
            lessonId = 6, type = "PILIHAN_GANDA",
            prompt = "Kula badhe siram kranjang menika.",
            secondaryPrompt = "Artikan kata 'siram' di dalam tingkat Krama (Catatan: siram di Krama Madya berarti menyiram, berbeda dengan mandinya orang tua)!",
            correctAnswer = "menyiram",
            wrongOptionsCsv = "mandi,melempar,membakar",
            phoneticText = "Ku-la ba-dhe si-ram kran-jang me-ni-ka"
        ))
        questions.add(Question(
            lessonId = 6, type = "PILIHAN_GANDA",
            prompt = "Bapak sampun kondur saking kantor.",
            secondaryPrompt = "Apa arti kata 'kondur'?",
            correctAnswer = "pulang",
            wrongOptionsCsv = "pergi,berdiri,masuk",
            phoneticText = "Ba-pak sam-pun kon-dur sa-king kan-tor"
        ))

        // --- LESSON 7 QUESTIONS (Krama Inggil Intro/Respect) ---
        questions.add(Question(
            lessonId = 7, type = "PILIHAN_GANDA",
            prompt = "Sugeng rawuh panjenengan wonten ing gubug kulo.",
            secondaryPrompt = "Artikan kalimat Krama Inggil menyambut tamu terhormat ini!",
            correctAnswer = "Selamat datang Anda di gubuk saya.",
            wrongOptionsCsv = "Selamat jalan kamu dari rumah saya.,Selamat sore Bapak/Ibu di kota ini.,Silakan berkunjung di pekarangan saya.",
            phoneticText = "Su-geng ra-wuh pan-je-ne-ngan won-ten ing gu-bug ku-lo"
        ))
        questions.add(Question(
            lessonId = 7, type = "SUSUN_KATA",
            prompt = "Panjenengan kepareng tindak pundi",
            secondaryPrompt = "Susun kalimat sangat halus: 'Anda berkenan pergi ke mana?'",
            correctAnswer = "Panjenengan kepareng tindak pundi",
            wrongOptionsCsv = "tindak,Panjenengan,kepareng,pundi,kowe,arep,lunga",
            phoneticText = "Pan-je-ne-ngan ke-pa-reng tin-dak pun-di?"
        ))
        questions.add(Question(
            lessonId = 7, type = "LISTENING",
            prompt = "Ibu nembe kemawon dahar wonten wingking.",
            secondaryPrompt = "Bandingkan 'mangan' (Ngoko), 'nedha' (Krama), dan 'dahar' (Krama Inggil). Semuanya berarti...",
            correctAnswer = "Makan",
            wrongOptionsCsv = "Tidur,Berbicara,Berjalan",
            phoneticText = "I-bu nem-be ke-ma-won da-har won-ten wing-king"
        ))
        questions.add(Question(
            lessonId = 7, type = "PILIHAN_GANDA",
            prompt = "Eyang kakung nembe kemawon sare.",
            secondaryPrompt = "Kosakata Krama Inggil untuk menghormati tidur kakek. 'Sare' artinya...",
            correctAnswer = "Tidur",
            wrongOptionsCsv = "Makan,Minum,Mandi",
            phoneticText = "E-yang ka-kung nem-be ke-ma-won sa-re"
        ))
        questions.add(Question(
            lessonId = 7, type = "PILIHAN_GANDA",
            prompt = "Sugeng kondur dhumateng ibu guru.",
            secondaryPrompt = "Artikan ungkapan penghormatan pamitan ini!",
            correctAnswer = "Selamat pulang kepada ibu guru.",
            wrongOptionsCsv = "Selamat datang kepada ibu guru.,Selamat pagi kepada ibu guru.,Selamat jalan untuk murid-murid.",
            phoneticText = "Su-geng kon-dur dhu-ma-teng i-bu gu-ru"
        ))

        // --- LESSON 8 QUESTIONS (Krama Inggil Body Parts) ---
        questions.add(Question(
            lessonId = 8, type = "PILIHAN_GANDA",
            prompt = "Mustaka bapak rumaos puyeng.",
            secondaryPrompt = "Artikan kalimat tentang kesehatan ayah ini! Mustaka adalah...",
            correctAnswer = "Kepala bapak terasa pusing.",
            wrongOptionsCsv = "Kaki bapak terasa linu.,Mata bapak terasa lelah.,Tangan bapak terasa gatal.",
            phoneticText = "Mus-ta-ka ba-pak ru-ma-os pu-yeng"
        ))
        questions.add(Question(
            lessonId = 8, type = "PILIHAN_GANDA",
            prompt = "Rikma simbah sampun putih sedaya.",
            secondaryPrompt = "Kosakata tubuh Krama Inggil. Arti dari 'rikma' adalah...",
            correctAnswer = "Rambut",
            wrongOptionsCsv = "Gigi,Kuku,Alis",
            phoneticText = "Rik-ma sim-bah sam-pun pu-tih se-da-ya"
        ))
        questions.add(Question(
            lessonId = 8, type = "SUSUN_KATA",
            prompt = "Pasuryan panjenengan ketingal sumringah",
            secondaryPrompt = "Susun kalimat penghormatan: 'Wajah Anda terlihat berseri-seri'",
            correctAnswer = "Pasuryan panjenengan ketingal sumringah",
            wrongOptionsCsv = "Pasuryan,panjenengan,ketingal,sumringah,rai,kowe,piye",
            phoneticText = "Pa-sur-yan pan-je-ne-ngan ke-ti-ngal sum-ri-ngah"
        ))
        questions.add(Question(
            lessonId = 8, type = "LISTENING",
            prompt = "Mripat (Ngoko) halus setingkat Krama Inggil dinamakan...",
            secondaryPrompt = "Pilih kata tinggi yang tepat untuk menghormati penglihatan orang tua!",
            correctAnswer = "Paningal",
            wrongOptionsCsv = "Talingan,Mustaka,Ampeyan",
            phoneticText = "Mri-pat dadi Pa-ni-ngal"
        ))
        questions.add(Question(
            lessonId = 8, type = "PILIHAN_GANDA",
            prompt = "Baja simbah nembe gerah.",
            secondaryPrompt = "Dalam bagian tubuh, 'Baja' adalah sebutan halus untuk 'Gigi'. Artikan kalimat ini!",
            correctAnswer = "Gigi nenek/kakek sedang sakit.",
            wrongOptionsCsv = "Tangan nenek sedang terluka.,Kaki kakek sedang terkilir.,Mata simbah sedang bengkak.",
            phoneticText = "Ba-ja sim-bah nem-be ge-rah"
        ))

        // --- LESSON 9 QUESTIONS (Krama Inggil Invitations & Ceremonies) ---
        questions.add(Question(
            lessonId = 9, type = "PILIHAN_GANDA",
            prompt = "Kulo nyuwun donga pangestu panjenengan sami.",
            secondaryPrompt = "Kalimat formal dalam hajatan. Arti kalimat ini adalah...",
            correctAnswer = "Saya mohon doa restu Anda sekalian.",
            wrongOptionsCsv = "Saya mau mengundang makan malam Anda.,Anda harus mendoakan adik saya saja.,Harap tenang saat acara dimulai.",
            phoneticText = "Ku-lo nyu-wun do-nga pa-nges-tu pan-je-ne-ngan sa-mi"
        ))
        questions.add(Question(
            lessonId = 9, type = "LISTENING",
            prompt = "Tindakipun bapak dhumateng pendopo upacara.",
            secondaryPrompt = "Kosakata pergerakan formal. 'Tindakipun' berarti...",
            correctAnswer = "Keberangkatan / kepergian Bapak",
            wrongOptionsCsv = "Kepulangan Bapak,Makan malamnya Bapak,Tidurnya Bapak",
            phoneticText = "Tin-da-ki-pun ba-pak dhu-ma-teng pen-do-po u-pa-ca-ra"
        ))
        questions.add(Question(
            lessonId = 9, type = "SUSUN_KATA",
            prompt = "Sugeng rawuh wonten upacara adat",
            secondaryPrompt = "Susun kalimat penyambutan adat: 'Selamat datang di upacara adat'",
            correctAnswer = "Sugeng rawuh wonten upacara adat",
            wrongOptionsCsv = "rawuh,Sugeng,wonten,upacara,adat,teka,ing,selamat",
            phoneticText = "Su-geng ra-wuh won-ten u-pa-ca-ra a-dat"
        ))
        questions.add(Question(
            lessonId = 9, type = "PILIHAN_GANDA",
            prompt = "Panjenengan kagungan kersa menopo?",
            secondaryPrompt = "Mengajukan pertanyaan pada tetua desa. Artikan kalimat kagungan kersa di atas!",
            correctAnswer = "Anda mempunyai kehendak/acara apa?",
            wrongOptionsCsv = "Anda baru pulang dari sawah kah?,Kamu mau pergi belanja ke mana?,Ada urusan apa dia kemari?",
            phoneticText = "Pan-je-ne-ngan ka-gu-ngan ker-sa me-no-po?"
        ))
        questions.add(Question(
            lessonId = 9, type = "PILIHAN_GANDA",
            prompt = "Mugi gusti berkahi tindak panjenengan.",
            secondaryPrompt = "Artikan kalimat berkati ini!",
            correctAnswer = "Semoga Tuhan memberkati perjalanan Anda.",
            wrongOptionsCsv = "Semoga kakek memberi makan Anda sekeluarga.,Semoga guru mengajari adik saya membaca.,Semoga Anda lancar belajar bahasa Jawa.",
            phoneticText = "Mu-gi gus-ti ber-ka-hi tin-dak pan-je-ne-ngan"
        ))

        questionDao.insertQuestions(questions)
        Log.d("LessonRepository", "Laid down ${questions.size} Javanese curriculum questions successfully.")
    }

    suspend fun completeLesson(lessonId: Int, xpEarned: Int) = withContext(Dispatchers.IO) {
        val userProgressDao = db.userProgressDao()
        val lessonDao = db.lessonDao()
        val xpLogDao = db.xpLogDao()

        // 1. Mark lesson completed
        lessonDao.updateLessonCompletion(lessonId, true)

        // 2. Fetch and update user progress (XP, Streak, Levels)
        var progress = userProgressDao.getUserProgress() ?: UserProgress()
        
        val currentTime = System.currentTimeMillis()
        var newStreak = progress.streak
        
        if (progress.lastActiveTime == 0L) {
            newStreak = 1
        } else {
            val diffMs = currentTime - progress.lastActiveTime
            val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)
            
            if (diffDays == 1L) {
                newStreak += 1
            } else if (diffDays > 1L) {
                newStreak = 1 // reset due to missing a day
            }
            // If diffDays is 0, same day, streak doesn't change
        }

        val totalXp = progress.xp + xpEarned
        // Level increases every 100 XP
        val newLevelIndex = (totalXp / 100) + 1

        val updatedProgress = progress.copy(
            xp = totalXp,
            streak = newStreak,
            lastActiveTime = currentTime,
            levelIndex = newLevelIndex,
            hearts = 5 // regenerate hearts on lesson completion as a reward!
        )
        userProgressDao.insertUserProgress(updatedProgress)

        // 3. Write XP log
        val lesson = db.lessonDao().getLessonById(lessonId)
        val sourceStr = if (lesson != null) "Selesai Kelas ${lesson.title}" else "Latihan Bahasa Jawa"
        xpLogDao.insertXpLog(XpLog(xpGained = xpEarned, source = sourceStr))
    }

    suspend fun deductHeart() = withContext(Dispatchers.IO) {
        val userProgressDao = db.userProgressDao()
        val progress = userProgressDao.getUserProgress() ?: return@withContext
        if (progress.hearts > 0) {
            val updated = progress.copy(hearts = progress.hearts - 1)
            userProgressDao.insertUserProgress(updated)
        }
    }

    suspend fun buyHearts() = withContext(Dispatchers.IO) {
        val userProgressDao = db.userProgressDao()
        val progress = userProgressDao.getUserProgress() ?: return@withContext
        if (progress.xp >= 30) {
            val updated = progress.copy(
                xp = progress.xp - 30,
                hearts = 5
            )
            userProgressDao.insertUserProgress(updated)
            db.xpLogDao().insertXpLog(XpLog(xpGained = -30, source = "Pemulihan Nyawa (5 Hati)"))
        }
    }

    suspend fun resetProgress() = withContext(Dispatchers.IO) {
        val userProgressDao = db.userProgressDao()
        val lessonDao = db.lessonDao()

        userProgressDao.insertUserProgress(UserProgress(id = 1))
        
        // Reset completion state of all lessons
        val allL = db.lessonDao().getAllLessonsFlow()
        // Run update query on all lessons
        for (i in 1..9) {
            lessonDao.updateLessonCompletion(i, false)
        }
        db.xpLogDao().insertXpLog(XpLog(xpGained = 0, source = "Reset Data Kemajuan"))
    }
}
