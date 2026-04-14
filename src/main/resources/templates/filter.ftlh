<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Фильтр сотрудников</title>

    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">

</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Filter page -->

    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

         <div class="bg-sky-900 px-6 logo-padding text-white text-center py-4">
             <h2 class="text-2xl font-semibold">Найти сотрудников</h2>
         </div>

        <!-- Filter Form -->
        <main class="flex flex-1 flex-col p-6 flex items-start">
            <div class="bg-white shadow-xl border border-gray-100 w-full max-w-md p-6 space-y-6">

                <form method="GET" action="${springMacroRequestContext.contextPath}/employees/show_employees" class="space-y-5">

                    <!-- First Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
                        <input type="text"
                               name="firstName"
                               placeholder="Например: Анна"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>

                    <!-- Last Name -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Фамилия</label>
                        <input type="text"
                               name="lastName"
                               placeholder="Например: Иванова"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>

                    <!-- Email -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                        <input type="text"
                               name="email"
                               placeholder="Например: anna@example.com"
                               class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500">
                    </div>
                    <!-- Position Filter -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Должность</label>
                        <select name="positionId" class="w-full border border-gray-300 shadow-sm py-2 px-3"
                                onchange="this.form.submit()">
                            <option value="">Все</option>
                            <#list positions as pos>
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>
                    <!-- Buttons -->
                    <div class="flex gap-3 pt-2">
                        <button type="submit"
                                class="flex-1 bg-sky-900 text-white py-2 px-4 font-medium hover:bg-sky-800 transition">
                            Найти
                        </button>
                        <!-- Кнопка сброса параметров фильтра -->
                        <a href="${springMacroRequestContext.contextPath}/employees/show_employees"
                           class="flex-1 bg-gray-200 text-gray-700 py-2 px-4 font-medium text-center hover:bg-gray-300 transition">
                            Сбросить
                        </a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>