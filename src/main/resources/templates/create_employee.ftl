<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Employee - HR Admin</title>
    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Alpine.js for interactivity -->
    <script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="css/hr_admin.css" rel="stylesheet">
</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Sidebar -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

         <div class="bg-sky-900 px-6 logo-padding text-white text-center py-4">
             <h2 class="text-2xl font-semibold">Введите данные о новом сотруднике</h2>
         </div>

        <div class="w-full max-w-6xl mx-0 bg-white shadow-xl border border-gray-100 all-employees">
            <div class="p-6 overflow-x-auto">
                <form action="/employees" method="post" class="p-0 bg-white rounded-lg space-y-4">
                    <!-- First Name -->
                    <div>
                        <label class="block text-base font-medium text-gray-700 mb-1">Имя</label>
                        <input
                            name="firstName"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                            autofocus
                        />
                    </div>

                    <!-- Last Name -->
                    <div>
                        <label class="block text-base font-medium text-gray-700 mb-1">Фамилия</label>
                        <input
                            name="lastName"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                        />
                    </div>

                    <!-- Email -->
                    <div>
                        <label class="block text-base font-medium text-gray-700 mb-1">Email</label>
                        <input
                            name="email"
                            type="email"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                        />
                    </div>

                    <!-- Position -->
                    <div>
                        <label class="block text-base font-medium text-gray-700 mb-1">Должность</label>
                        <select
                            name="position.id"
                            class="w-50 border border-gray-300 shadow-sm py-2 px-3 combo-btn focus:ring-2 focus:ring-sky-500 focus:outline-none"
                            required>
                            <#list positions as pos>
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>

                    <!-- Submit -->
                    <div class="flex gap-3 pt-2">
                        <button
                            type="submit"
                            class="bg-sky-900 w-48 py-3 px-4 font-medium text-white hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Сохранить
                        </button>
                        <a
                            href="/"
                            class="bg-gray-200 w-48 py-3 px-4 font-medium text-center hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Назад
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>