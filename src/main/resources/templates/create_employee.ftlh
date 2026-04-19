<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Employee - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">
</head>
<body class="bg-gray-50 min-h-screen flex">
    <!-- Create Employee page -->
    <!-- Sidebar -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

         <div class="bg-sky-900 px-6 logo-padding text-white text-center py-4">
             <h2 class="text-2xl font-semibold">Введите данные о новом сотруднике</h2>
         </div>

        <div class="w-full mx-0 bg-white shadow-xl border border-gray-100 all-employees">
            <div class="p-6 overflow-x-auto">
                <form action="${springMacroRequestContext.contextPath}/employees/" method="post" class="p-0 bg-white rounded-lg space-y-4">
                    <!-- First Name -->
                    <div>
                        <label for="firstName">Имя</label>
                        <input
                            id="firstName"
                            name="firstName"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                            autofocus
                        />
                    </div>

                    <!-- Last Name -->
                    <div>
                        <label for="lastName">Фамилия</label>
                        <input
                            id="lastName"
                            name="lastName"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                        />
                    </div>

                    <!-- Email -->
                    <div>
                        <label for="email">Email</label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            class="w-full border border-gray-300 shadow-sm py-2 px-3 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500"
                            required
                        />
                    </div>

                    <!-- Position -->
                    <div>
                        <label for="position_id">Должность</label>
                        <select
                            id="position_id"
                            name="position.id"
                            class="border border-gray-300 shadow-sm py-2 px-3 focus:ring-2 focus:ring-sky-500 focus:outline-none combo-btn"
                            style="width: 12em; padding-left: 1em; padding-right: 1em;"
                            required>
                            <#list positions as pos>
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>

                    <!-- Submit -->
                    <div class="flex gap-3 pt-2 justify-end">
                        <button type="submit"
                            class="bg-sky-900 w-36 py-3 px-4 font-medium text-white hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Сохранить
                        </button>
                        <a
                            href="${springMacroRequestContext.contextPath}/"
                            class="bg-gray-200 w-36 py-3 px-4 font-medium text-center hover:bg-sky-50 hover:text-sky-900 border-transparent hover:border-sky-900">
                            Назад
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>