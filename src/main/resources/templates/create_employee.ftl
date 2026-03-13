<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Employee - HR Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>body{font-family:'Inter',sans-serif;}.btn-dark-blue{@apply bg-sky-900 hover:bg-sky-800 text-white;}</style>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-4">
    <div class="w-full max-w-lg bg-white shadow-xl border border-gray-100">
        <div class="bg-sky-900 px-6 py-8 text-white text-center">
            <h2 class="text-2xl font-semibold">Добавление сотрудника</h2>
            <p class="text-sky-200">Введите данные о новом сотруднике</p>
        </div>
        <form action="/employees" method="post" class="p-6 space-y-6">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
                <input name="firstName" class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500" required autofocus />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Фамилия</label>
                <input name="lastName" class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500" required />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input name="email" type="email" class="w-full px-4 py-3 border border-gray-300 focus:ring-2 focus:ring-sky-500" required />
            </div>
            <div class="flex gap-3 pt-2">
                <button type="submit" class="btn-dark-blue w-full py-3 px-4 font-medium">Сохранить</button>
                <a href="/" class="btn-dark-blue w-full py-3 px-4 font-medium text-center">Назад</a>
            </div>
        </form>
    </div>
</body>
</html>
