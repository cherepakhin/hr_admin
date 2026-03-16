<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>All Employees - HR Admin</title>
    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .btn-dark-blue {
            @apply bg-sky-900 hover:bg-sky-800 text-white;
        }
        .action-icon {
            @apply w-5 h-5 transition-colors duration-200;
        }
        .action-edit {
            @apply text-sky-900 hover:text-sky-700;
        }
        .action-delete {
            @apply text-red-600 hover:text-red-800;
        }
        .pagination-link {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-medium;
        }
        .pagination-current {
            @apply px-3 py-1
                   bg-gradient-to-r from-red-500 to-red-700
                   text-white
                   font-bold
                   rounded
                   shadow-inner
                   border border-red-600
                   ring-1 ring-red-300;
        }
        .pagination-first-last {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-medium text-xs;
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen flex" x-data="{ sidebarOpen: false }" @keydown.window.escape="sidebarOpen = false">

    <!-- Sidebar Fragment -->
    <div
        :class="{ 'w-16': !sidebarOpen, 'w-64': sidebarOpen }"
        class="bg-white shadow-lg flex flex-col border-r border-gray-100 transition-all duration-300 overflow-hidden relative">

        <!-- Logo -->
        <div class="p-4 text-center border-b border-gray-100 flex items-center justify-center">
            <div class="w-8 h-8 bg-sky-900 text-white flex items-center justify-center font-bold rounded">
                HR
            </div>
            <span
                x-show="sidebarOpen"
                class="ml-3 text-sm font-semibold text-sky-900 whitespace-nowrap transition-opacity duration-300">
                Отдел кадров
            </span>
        </div>

        <!-- Navigation -->
        <nav class="mt-4 flex-1 px-2">
            <a href="/"
               class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"/>
                </svg>
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Сотрудники</span>
            </a>
            <a href="/employees/new"
               class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                </svg>
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Добавить</span>
            </a>
            <a href="/showEmployees"
               class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Показать всех</span>
            </a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <div class="w-full max-w-6xl mx-auto bg-white shadow-xl border border-gray-100">
            <div class="bg-sky-900 px-6 py-8 text-white text-center">
                <h2 class="text-2xl font-semibold">Список всех сотрудников</h2>
                <p class="text-sky-200">Страница ${currentPage + 1} из ${totalPages}, всего: ${totalElements} записей</p>
            </div>
            <div class="p-6 overflow-x-auto">
                <#if employees?has_content>
                    <table class="w-full text-sm text-left text-gray-700">
                        <thead class="uppercase border-t border-gray-100 bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 font-medium">Имя</th>
                                <th class="px-6 py-3 font-medium">Фамилия</th>
                                <th class="px-6 py-3 font-medium">Email</th>
                                <th class="px-6 py-3 font-medium text-center">Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list employees as emp>
                            <tr class="border-b border-gray-100 hover:bg-gray-50 transition">
                                <td class="px-6 py-4 font-medium">${emp.firstName}</td>
                                <td class="px-6 py-4">${emp.lastName}</td>
                                <td class="px-6 py-4">${emp.email}</td>
                                <td class="px-6 py-4 flex justify-center gap-4">
                                    <!-- Edit Action -->
                                    <a href="/employees/edit/${emp.id}"
                                       class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                       title="Редактировать">
                                        <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                        </svg>
                                    </a>

                                    <!-- Delete Action -->
                                    <a href="/employees/delete/${emp.id}"
                                       onclick="return confirm('Вы уверены, что хотите удалить сотрудника ${emp.firstName} ${emp.lastName}?');"
                                       class="action-delete inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-red-100 transition"
                                       title="Удалить">
                                        <svg class="action-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <div class="flex justify-between items-center mt-6">
                        <div class="text-sm text-gray-600">
                            Показано ${(currentPage * 10) + 1}–${(currentPage * 10) + employees?size} из ${totalElements}
                        </div>

                        <ul class="inline-flex space-x-1">
                            <!-- First Page Button -->
                            <#if currentPage gt 0>
                                <li>
                                    <a href="/showEmployees?page=0" class="pagination-first-last">≪</a>
                                </li>
                            <#else>
                                <li><span class="pagination-first-last opacity-50 cursor-not-allowed">≪</span></li>
                            </#if>

                            <!-- Previous Button -->
                            <#if currentPage gt 0>
                                <li>
                                    <a href="/showEmployees?page=${currentPage - 1}" class="pagination-link">←</a>
                                </li>
                            <#else>
                                <li><span class="pagination-link opacity-50 cursor-not-allowed">←</span></li>
                            </#if>

                            <!-- Page Numbers with Current Highlighted -->
                            <#assign startPage = [currentPage - 2, 0]?max />
                            <#assign endPage = [(startPage + 4), totalPages - 1]?min />

                            <#list startPage..endPage as p>
                                <li>
                                    <#if p == currentPage>
                                        <span class="pagination-current">${p + 1}</span>
                                    <#else>
                                        <a href="/showEmployees?page=${p}" class="pagination-link">${p + 1}</a>
                                    </#if>
                                </li>
                            </#list>

                            <!-- Next Button -->
                            <#if currentPage lt totalPages - 1>
                                <li>
                                    <a href="/showEmployees?page=${currentPage + 1}" class="pagination-link">→</a>
                                </li>
                            <#else>
                                <li><span class="pagination-link opacity-50 cursor-not-allowed">→</span></li>
                            </#if>

                            <!-- Last Page Button -->
                            <#if currentPage lt totalPages - 1>
                                <li>
                                    <a href="/showEmployees?page=${totalPages - 1}" class="pagination-first-last">≫</a>
                                </li>
                            <#else>
                                <li><span class="pagination-first-last opacity-50 cursor-not-allowed">≫</span></li>
                            </#if>
                        </ul>
                    </div>
                <#else>
                    <p class="text-center py-6 text-gray-500">Сотрудники не найдены.</p>
                </#if>
            </div>
            <div class="bg-gray-50 px-6 py-4 border-t border-gray-100 text-center">
                <a href="/" class="btn-dark-blue inline-flex items-center px-4 py-2 font-medium hover:bg-sky-50 hover:text-sky-900">← Назад</a>
            </div>
        </div>
    </div>

    <!-- Alpine.js for sidebar toggle -->
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
</body>
</html>